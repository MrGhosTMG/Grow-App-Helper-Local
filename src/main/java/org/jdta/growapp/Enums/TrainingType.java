package org.jdta.growapp.Enums;

import javafx.scene.paint.Color;

public enum TrainingType {
    LST("Low Stress Training", "Мягкое сгибание побегов", Color.HOTPINK),
    DEF("Defoliation", "Удаление листьев для\r проветривания", Color.LIME),
    SCRG("SCROG", "Screen of Green \r — сетка и равномерный рост", Color.ROYALBLUE),
    TOP("Toping", "Удаление верхушки для\r стимуляции роста боков", Color.RED),
    FIM("FIM + SCROG", "Мягкое прищипывание и сетка", Color.CYAN),
    ML("MainLining", "Создание симметрии роста", Color.DARKGREEN),
    OTHER("Other stresses", "Другое: супер крюк,\r бонсаи, насилие 😅", Color.LIGHTGRAY);

    private final String title;
    private final String description;
    private final Color color;

    TrainingType(String title, String description, Color color) {
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Color getColor() { return color; }
}

