/*
package com.example.heroesofiu3;

import android.util.Log;

public class GameState {
    private final GameField gameField;
    private Cell selectedCell;

    public GameState(int width, int height) {
        this.gameField = new GameField(width, height);
    }

    public GameField getGameField() {
        return gameField;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void selectCell(Cell cell){

        if (selectedCell==null) {
            selectedCell = cell;
        }
        else if (selectedCell == cell){
            selectedCell = null;
        }
        else{
            moveUnit(selectedCell, cell);
            selectedCell = null;
        }

    }

    private void moveUnit(Cell from, Cell to){
        if(to.getUnit() == null){
            to.setUnit(from.getUnit());
            from.setUnit(null);
        }
    }
}
*/
