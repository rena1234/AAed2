#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>
#include <sys/types.h>

Pag * criaPag(long int offsets [], long int offsetPai, int numChaves, int chaves[] ){

    Pag * pag = (Pag*) malloc(sizeof(Pag));
    pag -> offsetPai = offsetPai;
    pag -> numChaves = numChaves;

    // provisorio, soh pra teste se não alterar dá problema
    int i;
    for(i=0;i<3;i++){
        pag -> chaves[i] = chaves[i];
    }

     for(i=0;i<4;i++){
        pag -> offsets[i] = offsets[i];
    }
    escrevePag(pag);
}

void destroiPag(Pag * pag){
    free(pag);
}

long int achaEspacoLivre(){
    FILE * arqEspacoVazio;
    arqEspacoVazio = fopen("arqEspacoVazioPag","rb");

    long int offset;

    fseek(arqEspacoVazio,-sizeof(long int),SEEK_END);

    int ultPosicao = ftell(arqEspacoVazio);

    fread(&offset,sizeof(long int),1,arqEspacoVazio);
    fclose(arqEspacoVazio);

    truncate("arqEspacoVazioArv",ultPosicao);

    return offset;
}

void escrevePag(Pag * pag ){

    FILE * arqArvore = fopen("arqArvore");
    offset = achaEspacoLivre();
    if(offset == -2)
        fseek(arqArvore,0,SEEK_END);
    else
        fseek(arqArvore,offset,SEEK_SET);

    fwrite(&pag,sizeof(Pag),arqArvore);
}

