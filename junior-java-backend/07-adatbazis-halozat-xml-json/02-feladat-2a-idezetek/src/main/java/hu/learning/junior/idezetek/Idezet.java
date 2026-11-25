package hu.learning.junior.idezetek;

/**
 * Egy idézet: ki mondta és mit.
 *
 * @param szerzo  az idézet szerzője
 * @param szoveg  az idézet szövege
 * @param forras  honnan származik (pl. "quotable" vagy "wikiquote")
 */
public record Idezet(String szerzo, String szoveg, String forras) {

    /** @return olvasható, egysoros alak a konzolra */
    @Override
    public String toString() {
        return "„" + szoveg + "” – " + szerzo + " [" + forras + "]";
    }
}
