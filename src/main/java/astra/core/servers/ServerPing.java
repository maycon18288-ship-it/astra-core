package astra.core.servers;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ServerPing {

  private final int timeout;
  private final int maxRetries;
  private final long retryDelay;
  private JsonObject json;
  private final InetSocketAddress host;
  private String motd = null;
  private int max = 0;
  private int online = 0;
  private boolean isAvailable = false;
  private long lastPingTime = 0;
  private int failedAttempts = 0;

  @Builder
  public ServerPing(InetSocketAddress address, int timeout, int maxRetries, long retryDelay) {
    this.host = address;
    this.timeout = timeout > 0 ? timeout : 1000;
    this.maxRetries = maxRetries > 0 ? maxRetries : 3;
    this.retryDelay = retryDelay > 0 ? retryDelay : 1000;
  }

  public ServerPing(InetSocketAddress address) {
    this(address, 1000, 3, 1000);
  }

  public ServerPing(String ip, int port) {
    this(new InetSocketAddress(ip, port));
  }

  public CompletableFuture<Boolean> fetchAsync() {
    return CompletableFuture.supplyAsync(this::fetchWithRetry);
  }

  public boolean fetchWithRetry() {
    int attempts = 0;
    boolean success = false;

    while (attempts < maxRetries && !success) {
      try {
        if (attempts > 0) {
          Thread.sleep(retryDelay);
        }
        fetch();
        success = true;
      } catch (Exception e) {
        attempts++;
        failedAttempts++;
        if (attempts >= maxRetries) {
          handleFailure();
          return false;
        }
      }
    }

    if (success) {
      isAvailable = true;
      failedAttempts = 0;
      lastPingTime = System.currentTimeMillis();
    }

    return success;
  }

  private void fetch() throws IOException {
    try (Socket socket = new Socket()) {
      socket.setSoTimeout(timeout);
      socket.connect(host, timeout);

      DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      DataOutputStream handshake = new DataOutputStream(b);
      
      // Handshake packet
      handshake.writeByte(0x00);
      writeVarInt(handshake, 47);
      writeVarInt(handshake, host.getHostString().length());
      handshake.writeBytes(host.getHostString());
      handshake.writeShort(host.getPort());
      writeVarInt(handshake, 1);

      writeVarInt(dataOutputStream, b.size());
      dataOutputStream.write(b.toByteArray());

      // Status request
      dataOutputStream.writeByte(0x01);
      dataOutputStream.writeByte(0x00);
      
      DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
      readVarInt(dataInputStream);

      int id = readVarInt(dataInputStream);
      if (id == -1) {
        throw new IOException("Invalid packet ID received from " + host);
      }

      int length = readVarInt(dataInputStream);
      if (length == -1) {
        throw new IOException("Invalid packet length received from " + host);
      }

      byte[] in = new byte[length];
      dataInputStream.readFully(in);
      String json = new String(in);

      // Ping
      long now = System.currentTimeMillis();
      dataOutputStream.writeByte(0x09);
      dataOutputStream.writeByte(0x01);
      dataOutputStream.writeLong(now);

      readVarInt(dataInputStream);
      id = readVarInt(dataInputStream);
      if (id == -1) {
        throw new IOException("Invalid ping response from " + host);
      }

      this.json = new JsonParser().parse(json).getAsJsonObject();
      processData();
    }
  }

  private void handleFailure() {
    this.motd = null;
    this.online = 0;
    this.max = 0;
    this.isAvailable = false;
  }

  private void processData() {
    try {
      JsonObject players = json.get("players").getAsJsonObject();
      this.motd = json.get("description").getAsString();
      this.max = players.get("max").getAsInt();
      this.online = players.get("online").getAsInt();
    } catch (Exception e) {
      handleFailure();
    }
  }

  private int readVarInt(DataInputStream in) throws IOException {
    int i = 0, j = 0;
    while (true) {
      int k = in.readByte();
      i |= (k & 0x7F) << j++ * 7;
      if (j > 5) {
        throw new RuntimeException("VarInt too big");
      }
      if ((k & 0x80) != 128) {
        break;
      }
    }
    return i;
  }

  private void writeVarInt(DataOutputStream out, int value) throws IOException {
    while (true) {
      if ((value & 0xFFFFFF80) == 0) {
        out.writeByte(value);
        return;
      }
      out.writeByte(value & 0x7F | 0x80);
      value >>>= 7;
    }
  }

  public boolean isHealthy() {
    return isAvailable && 
           failedAttempts < maxRetries && 
           (System.currentTimeMillis() - lastPingTime) < TimeUnit.SECONDS.toMillis(30);
  }

  @Override
  public String toString() {
    return String.format("ServerPing{host=%s, motd='%s', online=%d, max=%d, available=%b}",
        host, motd, online, max, isAvailable);
  }
}
