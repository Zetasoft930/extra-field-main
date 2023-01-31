package com.fieldright.fr.util;

public class StringUtil {

    /**
     * @param fullText o texto completo
     * @param element  o elemento a ser considerado como ponto de partida
     * @return
     * @implNote Dado um texto e um elemento (texto) que ele contém, este método retorna a parte do texto à esquerda do elemento informado
     * @author Pacifique Mukuna
     */
    public static String getTextBeforeElement(String fullText, String element) {
        String textBefore = "";
        int index = fullText.indexOf(element);

        for (int i = 0; i < index; i++) {
            textBefore += fullText.charAt(i);
        }

        return textBefore;
    }

    /**
     * @param fullText o texto completo
     * @param element  o elemento a ser considerado como ponto de partida
     * @return
     * @implNote Dado um texto e um elemento (texto) que ele contém, este método retorna a parte do texto à direita do elemento informado
     * @author Pacifique Mukuna
     */
    public static String getTextAfterElement(String fullText, String element) {
        String textAfter = "";
        int elementSize = element.length();
        int index = fullText.indexOf(element);
        int start = index + elementSize;

        for (int i = start; i < fullText.length(); i++) {
            textAfter += fullText.charAt(i);
        }

        return textAfter;
    }
}
