package All;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

/*
 * C FEITO 100%
 * R FEITO 100%
 * U LOAD  
 * D FEITO 100%
 */
public class DataBase {
    
    static RandomAccessFile arq;
    static RandomAccessFile indices;
    static int contador;
    static long ponteiroatual;
    static long ponteiroAtualIndices;
    static Path DataDB;
    static Path indiceDB;
    static {
        try {
            arq = new RandomAccessFile("Dados\\Data.db", "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static Path indicesPath;
    static {
        try {
            indicesPath = Paths.get("Dados\\INDICE\\Indices.db");
            indices = new RandomAccessFile(indicesPath.toString(), "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
}

    //ABASTECE A DATABASE
    public static void CarregarDados(Path caminho) throws IOException {
        try {
            byte[] texto = Files.readAllBytes(caminho);
            String ler = new String(texto);
            
            //separa os objt a cada \n encontrado
            String[] trechos = ler.split("\n");
            
            for (String trecho : trechos) {
                //separa cada informação encontrada apos um ";"
                String[] Dados = trecho.split(";");
                
                if (Dados.length == 4) {
                    int ID = Integer.parseInt(Dados[0]);
                    String nome = Dados[1];
                    String platf = Dados[2];
                    
                    // Gerar um número aleatório entre 0 e 10000
                    Random random = new Random();
                    // Gera números de 1000 a 20000 inclusive
                    int numeroAleatorio = random.nextInt(900001) + 1000000;
                    //tratamento de jogos com ano nao informado
                    int ano;
                    if(Dados[3].trim().equalsIgnoreCase("N/A")) {
                        ano=-1;
                    }
                    else {
                        ano=Integer.parseInt(Dados[3].trim());
                    }
                    //cria um id novo apenas se nao tiver um ja salvo
                    if(ID<100000){
                        ID += numeroAleatorio;
                    }
                    Jogo jogo = new Jogo(ID, nome, platf, ano);
                    
                    //inserir no file.db
                    Create(jogo);
                } else {
                    System.out.println("Faltou dados");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    public static void CarregarDadosIndice(Path caminho) throws IOException {
        try {
            byte[] texto = Files.readAllBytes(caminho);
            String ler = new String(texto);
    
            // Separa os objetos a cada \n encontrado
            String[] trechos = ler.split("\n");
    
            for (String trecho : trechos) {
                // Separa cada informação encontrada após um ":"
                String[] Dados = trecho.split(":");
                
                if (Dados.length == 2) {
                    String posIDStr = Dados[0].trim();
                    String IDStr = Dados[1].trim();
    
                    // Verifique se posIDStr e IDStr são números antes de fazer a conversão
                    if (posIDStr.matches("\\d+") && IDStr.matches("\\d+")) {
                        long posID = Long.parseLong(posIDStr);
                        int ID = Integer.parseInt(IDStr);
                        System.out.println(posID+" "+ID);
                        CriarIndices(posID, ID);
                        System.out.println("FIZ 222");
                    } else {
                        System.err.println("Valor inválido encontrado na linha: " + trecho);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //METODOS PARA USO DO ARQUIVO DE INDICES
    private static void CriarIndices(long posID, int ID){
        try {
            if(ponteiroAtualIndices==0) {
                indices.seek(4);
            }
            indices.writeLong(posID);
            indices.writeInt(ID);
            //Encerra objto
            indices.writeChars(";");
            ponteiroAtualIndices=indices.getFilePointer();
            //Atualiza quantidade de jogos
            indices.seek(0);
            indices.writeInt(contador);
            //voltar pra pAtual
            indices.seek(ponteiroAtualIndices);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erro na criação de indices");
        }
    }
    //CREAT 
    public static void Create(Jogo jogo) {
        byte[] byteArray;
        long PosID;
        try {
            if(ponteiroatual==0) {
                arq.seek(4);
            }
            // insere no arquivo
            byteArray=jogo.toByteArray();
             //Posição inicial do jogo pro indice
            PosID=arq.getFilePointer();
            //lapide
            arq.writeChars("*");
            //tamanho do objeto
            arq.writeInt(byteArray.length);
        
            //objeto 
            arq.write(byteArray);
            arq.writeChars(";");
            ponteiroatual=arq.getFilePointer();
            //alterando a quantidade de jogos no arquivo
            contador+=1;
            //INDICE
            CriarIndices(PosID,jogo.getID());
            arq.seek(0);
            arq.writeInt(contador);
            arq.seek(ponteiroatual);

        } catch (Exception e) {
            // TODO: handle exception
        }
    } 
    private static void LerJogo(char x){
        Jogo jogo;
        try {
            int tamanhoObjeto = arq.readInt();
        //crio array pra receber os dados do jogo
            byte[] byteArray = new byte[tamanhoObjeto];

        //ler o dados e envia para o array e dps manda pra criar o jogo
            arq.read(byteArray);
            jogo=new Jogo();
            jogo.fromByteArray(byteArray);
            
            if(x=='*'){
                System.out.println(jogo);
            }else{
                System.out.println("---------------");
            }

        //encerra leitura de dados do jogo achando o ";"
            arq.readChar();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    //READ
    private static Jogo LerJogoParam(long aux) {
        Jogo jogo = null;
        try {
            arq.seek(aux);
            //lapide
            if(arq.readChar()=='*'){
                // Crio array pra receber os dados do jogo
                byte[] byteArray = new byte[arq.readInt()];
                // Ler o dados e envia para o array e dps manda pra criar o jogo
                arq.read(byteArray);
                jogo = new Jogo();
                jogo.fromByteArray(byteArray);
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return jogo;
    }
    private static long LerIndice(){
        long posição=-1;
        try {
            //Lê todos os dados pra cada objeto no arquivo 
            posição=indices.readLong();
            indices.readInt();
            indices.readChar();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return posição;
    }
    public static void GetAll() {
        System.out.println("\nTodos os jogos");
        try {
            indices.seek(0);
            int x = indices.readInt();
            for (int i = 0; i < x; i++) {
                //Le a posição do jogo no aquivo de indices
                long aux=LerIndice();
                //lê o jogo no arquivo usando o indice
                arq.seek(aux);
                char Lapide = arq.readChar();
                LerJogo(Lapide);
            }
        } catch (Exception e) {
            System.out.println("erro: " + e.getMessage());
        }
    }
    public static long getID(int ID) {
        long InicioArq = -1;
        try {
            // Le apartir do inicio
            indices.seek(0);
            int x = indices.readInt();
            for (int i = 0; i < x; i++) {
                long pontoInicial = indices.readLong(); // Obtenha o ponto inicial do registro atual
                if ( indices.readInt()== ID) {
                    System.out.println("Jogo encontrado :\n");
                    InicioArq = pontoInicial; 
                    break;
                } else {
                    //Pula pro proximo objeto (long + int + char)=14 bytes
                    indices.skipBytes(14);
                }
            }
            if (InicioArq == -1) {
                System.out.println("Jogo não encontrado");
            }
    
        } catch (Exception e) {
            System.out.println("erro: " + e.getMessage());
        }
        
        return InicioArq;
    }
    public static void getNome(String nome){
        try {
            //Le apartir do inicio
            indices.seek(0);
            int x=indices.readInt();
            for(int i=0;i<x;i++){
                Jogo jogo= LerJogoParam(LerIndice());
                //verificar se as Strings sao iguais
                if(jogo.getNome().equals(nome)){
                    System.out.println("\nJogo encontrado :\n"+jogo);
                    return; 
                }
            }
            System.out.println("Jogo nao encontrado");
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println("eerro");
        }
    }
    public static void getPlatAforma(String plataforma){
        try {
            //Le apartir do inicio
            indices.seek(0);
            int x=indices.readInt();
            for(int i=0;i<x;i++){
                Jogo jogo= LerJogoParam(LerIndice());
                //verificar se as Strings sao iguais
                if(jogo.getPlataforma().equalsIgnoreCase(plataforma)){
                    System.out.println("\nJogo encontrado :\n"+jogo);
                    return; 
                }
            }
            System.out.println("Jogo nao encontrado");
            return;
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println("eerro");
        }
    }
    public static void getAno(int ano){
        try {
            //Le apartir do inicio
            indices.seek(0);
            int x=indices.readInt();
            for(int i=0;i<x;i++){
                Jogo jogo= LerJogoParam(LerIndice());
                //verificar se as Strings sao iguais
                if(jogo.getAno()==ano){
                    System.out.println("\nJogo encontrado :\n"+jogo);
                    return; 
                }
            }
            System.out.println("Jogo nao encontrado");
            return;
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println("eerro");
        }
    }

    //UPDATE
    public static boolean UpdateGame(int ID) {
        Scanner scanner = new Scanner(System.in);
        long posGmae=getID(ID);
        Jogo jogo=null;
        try {
            arq.seek(posGmae);
            if(arq.readChar()=='*'){
                //Descobre o tamnho do vetor de bytes e cria o auxilixar para fazer as trocas
                int tamanho=arq.readInt();
                byte[] Jogoatual=new byte[tamanho];
                byte[] novoJogo;

                //Pega as novas informaçoes do jogo 
                System.out.print("Digite o nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite a plataforma: ");
                String plataforma = scanner.nextLine();
                System.out.print("Digite o ano: ");
                int ano = scanner.nextInt();
                jogo = new Jogo(ID,nome,plataforma,ano);
                novoJogo=jogo.toByteArray();

                //Se o jogo novo for <= ao atual apenas ira substituir os bytes
                if(novoJogo.length<Jogoatual.length){
                    for(int i=0;i<novoJogo.length;i++){
                        Jogoatual[i]=novoJogo[i];
                    }
                    arq.seek(posGmae);
                    arq.readChar();
                    arq.writeInt(Jogoatual.length);
                    arq.write(novoJogo);
                    System.out.println("Jogo atualizado com sucesso");
                }else{
                    System.out.println("Jogo criado no fim do arquivo");
                    Create(jogo);
                }
                System.out.println(jogo);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }
    //DELET
    public static boolean DeletGame(int ID) {
        try {
            arq.seek(0);
            int totalJogos = arq.readInt();
            int jogosExcluidos = 0;
    
            for (int i = 0; i < totalJogos; i++) {
                long posicaoAtual = arq.getFilePointer();
                char lapide = arq.readChar();
                if (lapide == '*') {
                    //******************* */
                    Jogo auxiliar = LerJogoParam(LerIndice());
                    if (auxiliar.getID() == ID) {
                        arq.seek(posicaoAtual);
                        arq.writeChars("-");
                        System.out.println(auxiliar+" (DELETADO)");
                        break;
                    }
                } else {
                    System.out.println("Nao encontrado");
                }
            }
            // Atualize o contador total de jogos e o contador de jogos excluídos
            arq.seek(0);
            arq.writeInt(totalJogos);
            arq.seek(ponteiroatual);
            contador -= jogosExcluidos;
    
            return jogosExcluidos > 0;
        } catch (Exception e) {
            System.out.println("erro: " + e.getMessage());
        }
        return false;
    }    

    //SALVAMENTO DE DADOS
    public static void SaveUsuario(String nomeArquivo) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(nomeArquivo));
            // Escreva o número total de jogos no arquivo
            writer.println("Toal de jogo: "+contador);
            
            // Percorra todos os jogos no arquivo de dados
            indices.seek(4); // Pule o contador no início do arquivo
            for (int i = 0; i < contador; i++) {
                Jogo jogo = LerJogoParam(LerIndice());
                writer.println(jogo.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close(); // Feche o arquivo
            }
        }
    }
    //Save usado para atualizar a DataBase
    public static void SaveFormatado(String nomeArquivo) {
        DataDB = Paths.get(nomeArquivo);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(nomeArquivo));
            // Percorra todos os jogos no arquivo de dados
            indices.seek(4); // Pula o contador no início do arquivo
            for (int i = 0; i < contador; i++) {
                Jogo jogo = LerJogoParam(LerIndice());
                // Escreva os dados do jogo no arquivo de texto
                writer.println(jogo.getID() + ";" + jogo.getNome() + ";" + jogo.getPlataforma() + ";" + jogo.getAno());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close(); // Feche o arquivo
            }
        }
    }
    public static void SaveIndices(String nomeArquivo) {
        indiceDB = Paths.get(nomeArquivo);
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(nomeArquivo));
            // Move para o início do arquivo de índices
            indices.seek(4);
            // Lê e escreve cada entrada de índice no arquivo de texto
            for (int i = 0; i < contador; i++) {
                long posID = indices.readLong();
                int ID = indices.readInt();
                // Escreve a entrada de índice no arquivo de texto
                escritor.println(posID + ":" +ID);
                //le o fim o objeto
                indices.readChar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (escritor != null) {
                escritor.close(); // Fecha o arquivo
            }
        }
    }
    
}
