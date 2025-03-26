package app.product;

public enum UnitOfMeasure implements Comparable<UnitOfMeasure> {
    METERS("метры"),
    CENTIMETERS("сантиметры"),
    PCS("штуки"),
    LITERS("литры"),
    MILLIGRAMS("миллиграммы");

    private String naming;

    UnitOfMeasure(String naming) {
        this.naming = naming;
    }
}
