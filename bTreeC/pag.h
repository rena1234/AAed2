
#ifndef PAGNO_H_INCLUDED
#define PAGNO_H_INCLUDED

typedef struct Pag{

    //Esse numero de chaves é provisorio
    //mudar para algum define na versão final
    int chaves [3];
    long int offsets[4];
    int numChaves;
    long int offsetPai;
}

#endif
