
#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>
#include <sys/types.h>

#include "arvoreB.h"
#include "cell.h"

Folha * criaFolha(long int offsetProx, long int offsetPai,int numChaves, Cell celulas [] ){

    Folha * folha = (Folha*) malloc(sizeof(Folha));

    folha -> offsetProx = offsetProx;
    folha -> offsetPai = offsetPai;
    folha -> numChaves = numChaves;

    int i;
    for(i=0;i<numChaves;i++)
        folha -> celulas[i] = celulas[i];
    for(i=i;i<NMenos1;i++)
        folha -> celulas[i] = -1;
    return folha;
}

void destroiFolha(Folha * folha){
    free(folha);
}

long int  achaEspacoLivre(){

    FILE * arqEspacoVazio;
    arqEspacoVazio = fopen("arqEspacoVazioNo","rb");

    long int offset;

    fseek(arqEspacoVazio,-sizeof(long int),SEEK_END);

    int ultPosicao = ftell(arqEspacoVazio);

    fread(&offset,sizeof(long int),1,arqEspacoVazio);
    fclose(arqEspacoVazio);

    truncate("arqEspacoVazioNo",ultPosicao);

    return offset;
}

void escreveFolhaNova(Folha * folha){
    FILE * arqArvore = fopen("arqArvore");
    offset = achaEspacoLivre();
    if(offset == -2)
        fseek(arqArvore,0,SEEK_END);
    else
        fseek(arqArvore,offset,SEEK_SET);

    fwrite(&folha,sizeof(Folha),arqArvore);
}

void insereItemFolha(Cell * celula, long int offsetPag, Folha * folha){
    if(folha -> numChaves == NMenos1){
        //que tesao, que viagem
    }
    else{
        int indAnterior = achaPosicaoCell(folha,celula);
        if( indAnterior == -1)
            insereIni(celula,folha);
        else
            inserePosicao(celula,indAnterior,folha);
    }
    //FALTA ESCREVER AINDA (ESCREVE NAS SUB)
}

int achaPosicaoCell(Folha * folha, Cell * celula){
    int ind = folha -> indiceCell1; int indAnterior;
    while( (folha -> celulas[ind].chave) < (celula -> chave) ){
        indAnterior = ind;
        ind = folha -> celulas[ind].proxCell;
    }

    //QND A NOVA CELULA É  a cell inicial
    if( (folha -> celulas[indiceCell1].chave) > (celula -> chave) )
        indAnterior = -1;

    return indAnterior;
}
void insereIni(Cell * celula, long int offsetPag, Folha * folha){
    folha -> celulas[numChaves] = *celula;
    folha -> celulas[numChaves].proxCell = folha -> indiceCell1;
    folha -> indiceCell1 = folha -> numChaves;
    folha -> numChaves++;
}

void inserePosicao(Cell * celula, long int offsetPag, Folha * folha){
    //MUDAR NOME PARA RET IND ANTERIOR
    indAnterior = achaPosicaoCell(folha,celula);
    folha -> celulas[numChaves] = *celula;
    folha -> celulas[numChaves].proxCell = folha -> celulas[indAnterior].proxCel;
    folha -> celulas[indAnterior].proxCell = folha -> numChaves;
    folha -> numChaves++;
}

void insereSplit(Cell * celula, long int offsetPag, Folha * folha){
    Cell celulasPag1[NMenos1];
    Cell celulasPag2[NMenos1];


    int indAnterior = achaPosicaoCell(folha,celula);

    int indiceCellPostNova;
    if (indAnterior == -1)
        indiceCellPostNova = folha -> indiceCell1;
    else
        int indiceCellPostNova = folha -> celulas[indAnterior].proxCell;

    int indAtual = folha -> indiceCell1;
    int i; bool novaCelulaNaoEscrita = true;
    for (i=0;i<NMenos1/2;i++){
        if(indAtual == indiceCellPostNova && novaCelulaNaoEscrita){
            celulasPag1[i] = *celula;
            celulasPag1[i].proxCell = i+1;
            novaCelulaNaoEscrita = false;
            continue;
        }
        celulasPag1[i] = folha -> celulas[indAtual];
        indAtual = celulasPag1[i].proxCell;
        celulasPag1[i].proxCell = i + 1;
    }

     for (i=0,j=NMenos1/2;j<NMenos1;i++,j++){
        if(indAtual == indiceCellPostNova && novaCelulaNaoEscrita){
            celulasPag2[i] = *celula;
            celulasPag2[i].proxCell = i+1;
            novaCelulaNaoEscrita = false;
            continue;
        }
        celulasPag2[i] = folha -> celulas[indAtual];
        indAtual = celulasPag2[i].proxCell;
        celulasPag2[i].proxCell = i + 1;
    }

    //TEM QUE PROPAGAR UMA CHAVE PRA CIMA E ESCREVER AS DUAS FOLHAS
    //NO ARQUIVO
}

void splitCriaNovoPai(){

}
