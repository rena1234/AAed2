import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by gabriel on 02/09/16.
 */
public class Registro {


    private int matricula;
    private String nome;
    private int ano;
    private int semestre;
    private int codigo_curso;

    public Registro(int matricula, int ano, int semestre, int codigo_curso, String nome) {
        this.setMatricula(matricula);
        this.setNome(nome);
        this.setAno(ano);
        this.setSemestre(semestre);
        this.setCodigo_curso(codigo_curso);
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCodigo_curso() {
        return codigo_curso;
    }

    public void setCodigo_curso(int codigo_curso) {
        this.codigo_curso = codigo_curso;
    }


}
