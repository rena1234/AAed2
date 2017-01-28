
#ifndef FOLHA_H_INCLUDED
#define FOLHA_H_INCLUDED

typedef struct Folha{

    long int offsetProx;
    long int offsetPai;
    Cell celulas [NMenos1];
    int numChaves;
    int indiceCell1;
}

Folha * criaFolha(long int offsetProx, long int offsetPai,int numChaves, Cell celulas [] );
Folha * destroiFolha(Folha * no);

#endif
