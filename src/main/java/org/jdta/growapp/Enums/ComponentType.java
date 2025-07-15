package org.jdta.growapp.Enums;

public enum ComponentType {

    PERLITE("Limestone"),
    PEAT("Peat"),
    SAND("Sand"),
    LOAMS("Loams"),
    SOIL("Black Soil"),
    FERTILIZED_SOIL("Soil with NPK"),
    COCONUT_HUSK("Coconut fiber");


    private final String componentName;

    ComponentType(String componentName) {
        this.componentName = componentName;
    }


    @Override
    public String toString() {
        return componentName;
    }
}
