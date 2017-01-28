#ifndef LINKEDLIST_H_INCLUDED
#define LINKEDLIST_H_INCLUDED

typedef struct noLong{
    long int offset;
    noLong * prox;
}NOLONG;

typedef struct linkedListLong{
    noLong * head;
}LINKEDLIST;

void destroiListaLong(linkedListLong * lista);
void criaListaLong(noLong * head);

#endif


