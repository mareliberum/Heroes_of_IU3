/*
package com.example.heroesofiu3;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    private final Integer width;
    private final Integer height;
    private final List<List<Cell>> cells;

    public GameField(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.cells = new ArrayList<>();

        for (int x = 0; x < width; x++){
            List<Cell> column = new ArrayList<>();
            for (int y = 0; y < height; y++){
                column.add(new Cell(x,y));
            }
            cells.add(column);
        }
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        if(x >= 0 && x <= width && y >= 0 && y <= height){
            return cells.get(x).get(y);
        }
        return null;
    }
}
*/
