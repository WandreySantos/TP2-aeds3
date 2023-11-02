package All;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Jogo {
	private int ID;
	private String nome;
	private String plataforma;
	private int ano;
	
	Jogo(){
		
	}
	Jogo(int ID,String nome,String plataforma,int ano){
		this.ID=ID;
		this.nome=nome;
		this.plataforma=plataforma;
		this.ano=ano;
	}
	public String toString() {
        return "ID: " + ID +", Nome: "+nome+", Plataforma: "+plataforma+", Ano: "+ano;
    }
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
	
		dos.writeInt(ID);
		dos.writeUTF(nome);
		dos.writeUTF(plataforma);
		dos.writeInt(ano);
		return baos.toByteArray();
	}
	public void fromByteArray(byte[] ba) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		DataInputStream dis = new DataInputStream(bais);
		ID = dis.readInt();
		nome = dis.readUTF();
		plataforma = dis.readUTF();
		ano = dis.readInt();
	}
	public void fromByteArray2(byte[] ba) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(ba);
			DataInputStream dis = new DataInputStream(bais);
			ID = dis.readInt();
			nome = dis.readUTF();
			plataforma = dis.readUTF();
			ano = dis.readInt();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getPlataforma() {
		return plataforma;
	}
	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}
	
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
}
