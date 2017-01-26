/**
 * Created by gabriel on 04/09/16.
 */
public class Busca {
    public static void buscaRegistro(int matricula){
        AVLTree tree = AVLTree.openTree();
        long pos = tree.search(matricula);
        if(pos != -1){
            System.out.println("Nome: "+ Entrada.leNome("teste1",pos)
            +"\nAno: "+Entrada.leAno("teste1",pos)
            +"\nSemestre: "+Entrada.leSem("teste1",pos)
            +"\nCodigo_turma: "+Entrada.leCod("teste1",pos));
        }else{
            System.out.println("O registro com a matricula informada nao existe!");
        }
    }
}
