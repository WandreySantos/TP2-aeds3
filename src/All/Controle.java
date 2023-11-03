package All;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Controle {
    
    static Scanner scanner = new Scanner(System.in);
    public static void opcoes() {
        System.out.println("1 - Adicionar jogos");
        System.out.println("2 - Mostrar todos os jogos");
        System.out.println("3 - Buscar jogo");
        System.out.println("4 - Alterar dados");
        System.out.println("5 - Excluir Jogo");
        System.out.println("6 - Compactar arquivops atuais");
        System.out.println("7 - Descompactar arquivos selecionados");
        System.out.println("0 - Sair");
    }
    public static void AdiçãoDeJogos(){
        System.out.println("Quantos jogos serao adicionados?");
                        int quantidade = scanner.nextInt();
                        scanner.nextLine(); // Consumir a quebra de linha
                        for (int i = 0; i < quantidade; i++) {
                            Jogo jogo = new Jogo();

                            System.out.println("Jogo "+(i+1)+")");
                            System.out.print("Digite o ID: ");
                            int ID = scanner.nextInt();
                            scanner.nextLine(); // Consumir a quebra de linha
                            System.out.print("Digite o nome: ");
                            String nome = scanner.nextLine();
                            System.out.print("Digite a plataforma: ");
                            String plataforma = scanner.nextLine();
                            System.out.print("Digite o ano: ");
                            int ano = scanner.nextInt();

                            jogo = new Jogo(ID,nome,plataforma,ano);

                            // Adicione o jogo ao banco de dados
                            DataBase.Create(jogo);
                            System.out.println("Criado com sucesso\n"+jogo);
                        }
    }
    public static void MenuDeBusca(){
        int op = -1;
        while (op != 0) {
            System.out.println("\nMENU DE BUSCA");
            System.out.println("\nQual a forma de busca:\n1 - ID\n2 - Nome\n3 - Pl"
            +"ataforma\n4 - Ano\n0 - Voltar");
            System.out.print("Esolha: ");
            op = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            switch (op) {
                                case 1:
                                    System.out.print("Digite o ID a ser pesquisado: ");
                                    int searchID = scanner.nextInt();
                                    DataBase.getID(searchID);
                                    break;
                                case 2:
                                    System.out.print("Digite o nome a ser pesquisado: ");
                                    String searchName = scanner.nextLine();
                                    DataBase.getNome(searchName);
                                    break;
                                case 3:
                                    System.out.print("Digite a plataforma: ");
                                    String plataforma = scanner.next();
                                    DataBase.getPlatAforma(plataforma);
                                    break;
                                case 4:
                                    System.out.print("Digite o ano para filtrar: ");
                                    int ano = scanner.nextInt();
                                    DataBase.getAno(ano);
                                    break;
                                case 0:
                                    System.out.println("Voltando para o Menu Principal.");
                                    break;
                                default:
                                    System.out.println("Opção inválida. Por favor, selecione uma opção válida.");
                                    break;
                            }
                        }
    }
    public static void SalvarArquivos(){
        DataBase.SaveFormatado("Dados\\Dados_Salvos.txt");
        DataBase.SaveUsuario("Dados\\Data_Base_Usuario.txt");
        DataBase.SaveIndices("Dados\\INDICE\\indices_Salvos.txt");
    }
    public static void Compactar() {
        System.out.println("Dé o nome do arquivo");
        String name= scanner.nextLine();
        Path PastaBACKUP =Paths.get("Dados", "BACKUP");
        if(Files.notExists(PastaBACKUP)){
            try {
                Files.createDirectories(PastaBACKUP);
            } catch (Exception e) {
                // TODO: handle exception
                return;
            }
        }
        Path caminhoArquivoBACKUP =PastaBACKUP.resolve(name+".txt");
            try {
                Files.createFile(caminhoArquivoBACKUP);
                System.out.println("Arquivo compatactdo criado com sucesso");
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Erro ao criar arquivo compactado");
            }
    }
    public static void main(String[] args) {
        try {
            Path caminhoNovo = Paths.get("BASE 2.txt");
            Path caminhoAtual = Paths.get("Dados\\Dados_Salvos.txt");
            Path caminhoAtualIndice = Paths.get("Dados\\INDICE\\indices_Salvos.txt");
            //Mantem  a pasta .db sempre atualizada
            Path arquivoDataDB = Paths.get("Dados\\Data.db");
            Path arquivoIndiceDB = Paths.get("Dados\\INDICE\\Indices.db");
                Files.deleteIfExists(arquivoDataDB);
                Files.deleteIfExists(arquivoIndiceDB);
                // Recriar o arquivo Data.db
                Files.createFile(arquivoDataDB);
                Files.createFile(arquivoIndiceDB);

            System.out.println("1 - Caso queria resetar a DataBase\n2 - Para continuar ");
            int y=scanner.nextInt();
            if(y==1){
                DataBase.CarregarDados(caminhoNovo);
            }else{
                DataBase.CarregarDados(caminhoAtual);
                DataBase.CarregarDadosIndice(caminhoAtualIndice);
            }
            int x = -1;
            while (x != 0) {
                System.out.println("\nMENU PRINCIPAL");
                opcoes();
                System.out.print("\nDigite sua escolha: ");
                x = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
                
                switch (x) {
                    case 1:
                        AdiçãoDeJogos();
                        SalvarArquivos();
                        break;
                    case 2:
                        DataBase.GetAll();
                        break;
                    case 3:
                        MenuDeBusca();
                        break;
                    case 4:
                        System.out.println("Digite ID do jogo a ser atualizado");
                        int IDatt = scanner.nextInt();
                        scanner.nextLine(); // Consumir a quebra de linha
                        DataBase.UpdateGame(IDatt);
                        SalvarArquivos();
                        break;
                    case 5:
                        System.out.println("Digite ID do jogo a ser exluido");
                        int IDrm = scanner.nextInt();
                        scanner.nextLine(); // Consumir a quebra de linha
                        DataBase.DeletGame(IDrm);
                        System.out.println("aQUI?");
                        break;
                    case 6:
                        Compactar();
                        break;
                    case 0:
                        System.out.println("Encerrando o programa.");
                        SalvarArquivos();
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, selecione uma opção válida.");
                        break;
                }
            }
            System.out.println("Dados salvos em " + caminhoAtual);
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
