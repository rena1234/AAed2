
#ifndef PAGNO_H_INCLUDED
#define PAGNO_H_INCLUDED


typedef struct Cell{
    int chave; //Provisorio, representa dados em geral
    int proxCell;
}CELL;

Cell * criaCell(int proxCell, int data /*provisorio*/);

#endif
