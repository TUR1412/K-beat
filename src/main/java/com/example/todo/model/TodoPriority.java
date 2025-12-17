package com.example.todo.model;

public enum TodoPriority {
    HIGH("高", 3, "priority-high"),
    MEDIUM("中", 2, "priority-medium"),
    LOW("低", 1, "priority-low");

    private final String label;
    private final int weight;
    private final String cssClass;

    TodoPriority(String label, int weight, String cssClass) {
        this.label = label;
        this.weight = weight;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public int getWeight() {
        return weight;
    }

    public String getCssClass() {
        return cssClass;
    }
}
