
#include <stdio.h>

#include <unistd.h>
#include <sys/types.h>

//long intte 4bytes nesse pc

int main(){
/*
    FILE * arqTeste;
    arqTeste = fopen("teste","wb");

    long int x = 1;
    long int y = 2;
    long int z = 4;

    fwrite(&x,sizeof(long int),1,arqTeste);
    fwrite(&y,sizeof(long int),1,arqTeste);
    fwrite(&z,sizeof(long int),1,arqTeste);

    fclose(arqTeste);

    arqTeste = fopen("teste","rb");

    fseek(arqTeste,-sizeof(long int),SEEK_END);
    fread(&x,sizeof(long int),1,arqTeste);

    printf("%ld\n",x);

    fseek(arqTeste,-(sizeof(long int)),SEEK_END);
    long int position = ftell(arqTeste);

    fclose(arqTeste);

    truncate("teste", position);

    arqTeste = fopen("teste","rb");

    fseek(arqTeste,-sizeof(long int),SEEK_END);
    fread(&x,sizeof(long int),1,arqTeste);

    printf("%ld\n",x);

    fclose(arqTeste);
    */

    typedef struct teste{
        int num;
    }Teste;

    Teste * t1;
    t1 -> num = 2;

    int num = t1 -> num;

    printf("%p %p", t1.num, &num);

}
