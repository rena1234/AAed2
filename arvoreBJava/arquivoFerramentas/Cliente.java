package arquivoFerramentas;

import arvoreB.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

/**
 * Created by gabriel on 11/02/17.
 */
public class Cliente{

    private String cpf;
    private String nome;
    private String endereco;
    private String email;
    private String data_nascimento;
    private String telefone;
    private int num_compras;

    public Cliente(){

    }
    public Cliente(String cpf,String nome,String endereco,String email,String data_nascimento,String telefone, int num_compras){
        this.setCpf(cpf);
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setEmail(email);
        this.setData_nascimento(data_nascimento);
        this.setTelefone(telefone);
        this.setNum_compras(num_compras);
    }
    public void escreveRegistro(RandomAccessFile file){
        String cpf1 = this.getCpf();
        String cpf2= cpf1.replaceAll("[\\D]","");
        long cpfNum = Long.parseLong(cpf2);
            try {
                file.seek(file.length());
                byte[] cpf = this.getCpf().getBytes();
                file.write(cpf);
                file.writeChar('\t');
                byte[] nome = this.getNome().getBytes();
                file.write(nome);
                file.writeChar('\t');
                byte[] endereco = this.getEndereco().getBytes();
                file.write(endereco);
                file.writeChar('\t');
                byte[] email = this.getEmail().getBytes();
                file.write(email);
                file.writeChar('\t');
                byte[] data_nascimento = this.getData_nascimento().getBytes();
                file.write(data_nascimento);
                file.writeChar('\t');
                byte[] telefone = this.getTelefone().getBytes();
                file.write(telefone);
                file.writeChar('\t');
                String num = Integer.toString(this.getNum_compras());
                byte[] num1 = num.getBytes();
                file.write(num1);
                file.writeChar('\n');

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static void removerRegistro(RandomAccessFile file, long offset){
            try {
                file.seek(offset);
                String cpfString = "000.000.000-00";
                byte [] cpfBytes = cpfString.getBytes();
                file.write(cpfBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public static  Cliente leRegistro(RandomAccessFile file, long offset){
        Cliente c = new Cliente();
        try {
            file.seek(offset);
            String iso = file.readLine();
            String utf8 = new String(iso.getBytes("ISO-8859-1"), "UTF-8");
            StringTokenizer registro = new StringTokenizer(utf8,"\t");
            c.setCpf(registro.nextToken());
            c.setNome(registro.nextToken());
            c.setEndereco(registro.nextToken());
            c.setEmail(registro.nextToken());
            c.setData_nascimento(registro.nextToken());
            c.setTelefone(registro.nextToken());
            String num = registro.nextToken();
            c.setNum_compras(Integer.parseInt(num));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return c;
    }
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getNum_compras() {
        return num_compras;
    }

    public void setNum_compras(int num_compras) {
        this.num_compras = num_compras;
    }

}
