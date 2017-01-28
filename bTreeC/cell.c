#include <stdio.h>

Cell * criaCell(int proxCell, int data /*provisorio*/){
    Cell * cell = (Cell *)malloc(sizeof(Cell));
    cell -> data = data;
    cell -> proxCell = proxCell;
}
