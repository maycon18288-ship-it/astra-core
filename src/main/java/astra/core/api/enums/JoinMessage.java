package astra.core.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum JoinMessage {

    NENHUMA("Nenhuma", JoinMessageCategory.GERAL,
            "Padrão",
            Collections.singletonList(
                    ""
            ),"§7Mensagem padrão de entrada.",
           "", false, ""),
           
    CHEGOUCOMTUDO("Chegou com tudo", JoinMessageCategory.RANKS,
        "Chegou com tudo",
        Arrays.asList(
                "",
                "§6§m-------------------------------",
                "{player} §6chegou com tudo!",
                "§6§m-------------------------------",
                ""
        ),
                "{player} §6chegou com tudo!",
        "joinmessage.chegou_com_tudo", true, "§eArcanjo§7."),

    TANAAREA("Tá na área", JoinMessageCategory.RANKS,
        "Tá na área",
                    Arrays.asList(
                            "",
                            "§6§m-------------------------------",
                            "{player} §6tá na área!",
                            "§6§m-------------------------------",
                            ""
    ),
                "{player} §6tá na área!",
                        "joinmessage.ta_na_area", true, "§eArcanjo§7."),

    SANGUE("Sangue nos olhos", JoinMessageCategory.RANKS,
            "Sangue nos olhos",
            Arrays.asList(
                    "",
                    "§6§m---------------------------------------",
                    "{player} §6chegou com sangue nos olhos!",
                    "§6§m---------------------------------------",
                    ""
            ),
            "{player} §6chegou com sangue nos olhos!",
            "joinmessage.sangue_nos_olhos", true, "§eArcanjo§7."),

    EXPLOSAO("Explosão", JoinMessageCategory.RANKS,
            "Explosão",
                         Arrays.asList(
                                 "",
                                 "§6§m-------------------------------",
                                 "{player} §6explodiu o lobby!",
                                 "§6§m-------------------------------",
                                 ""
    ),
            "{player} §6explodiu o lobby!",
                    "joinmessage.explosao", true, "§5Serafim§7."),

    HALLOWEEN("Halloween", JoinMessageCategory.RANKS,
            "Halloween",
            Arrays.asList(
                    "",
                    "§6§m-------------------------------",
                    "{player} §6assombrou o lobby!",
                    "§6§m-------------------------------",
                    ""
            ),
            "{player} §6assombrou o lobby!",
            "joinmessage.halloween", true, "§cQuerubim§7."),

    LUTA("Luta", JoinMessageCategory.RANKS,
            "Luta",
            Arrays.asList(
                    "",
                    "§6§m--------------------------------",
                    "{player} §6chegou para lutar!",
                    "§6§m--------------------------------",
                    ""
            ),
            "{player} §6chegou para lutar!",
            "joinmessage.luta", true, "§5Serafim§7."),

    PARAQUEDAS("Paraquedas", JoinMessageCategory.RANKS,
            "Paraquedas",
            Arrays.asList(
                    "",
                    "§6§m-------------------------------",
                    "{player} §6caiu de paraquedas!",
                    "§6§m-------------------------------",
                    ""
            ),
            "{player} §6caiu de paraquedas!",
            "joinmessage.paraquedas", true, "§eArcanjo§7."),

        DIVERSAO("Diversão", JoinMessageCategory.RANKS,
            "Diversão",
            Arrays.asList(
                    "",
                    "§6§m----------------------------------------",
                    "{player} §6chegou e a diversão começou!",
                    "§6§m----------------------------------------",
                    ""
            ),
            "{player} §6chegou e a diversão começou!",
            "joinmessage.diversao", true, "§cQuerubim§7."),

        AMEDRONTANDO("Amedrontando", JoinMessageCategory.RANKS,
            "Amedrontando",
            Arrays.asList(
                    "",
                    "§6§m------------------------------------",
                    "{player} §6chegou amedrontando!",
                    "§6§m------------------------------------",
                    ""
            ),
            "{player} §6chegou amedrontando!",
            "joinmessage.amedrontando", true, "§cQuerubim§7."),

    BAGUNÇA("Bagunça", JoinMessageCategory.RANKS,
            "Bagunça",
            Arrays.asList(
                    "",
                    "§6§m---------------------------------",
                    "{player} §6gosta de bagunçar!",
                    "§6§m---------------------------------",
                    ""
            ),
            "{player} §6gosta de bagunça!",
            "joinmessage.bagunça", true, "§cQuerubim§7."),

    QUEBRADEIRA("Quebradeira", JoinMessageCategory.RANKS,
            "Quebradeira",
            Arrays.asList(
                    "",
                    "§6§m---------------------------------",
                    "{player} §6chegou quebrando tudo!",
                    "§6§m---------------------------------",
                    ""
            ),
            "{player} §6chegou quebrando tudo!",
            "joinmessage.quebradeira", true, "§cQuerubim§7.");

    private final String displayName;
    private final JoinMessageCategory category;
    private final String name;
    @Getter private final List<String> messages;
    private final String description;
    private final String permission;
    private final boolean exclusive;
    private final String exclusiveFor;

    public static JoinMessage getJoinMessage(String name) {
        try {
            return JoinMessage.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return NENHUMA;
        }
    }

    public static List<JoinMessage> getMessagesByCategory(JoinMessageCategory category) {
        return Arrays.stream(values())
                .filter(msg -> msg.getCategory() == category)
                .collect(Collectors.toList());
    }

    public static List<JoinMessage> getAllMessages() {
        return Arrays.asList(values());
    }

    public enum JoinMessageCategory {
        GERAL,
        RANKS,
        ESPECIAIS
    }
}