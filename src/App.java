import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = null;
        Scanner arquivo = null;
        String linha;
        int numProdutos;
        Produto produto;
        int i;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            
            // Lê a quantidade de produtos já existentes no arquivo
            numProdutos = Integer.parseInt(arquivo.nextLine());
            
            // Instancia o vetor com o tamanho dos produtos do arquivo + espaço reserva
            vetorProdutos = new Produto[numProdutos + MAX_NOVOS_PRODUTOS];
            
            for (i = 0; i < numProdutos; i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                vetorProdutos[i] = produto;
            }
            quantosProdutos = i;
            
        } catch (IOException excecaoArquivo) { 
            System.out.println("Erro ao ler arquivo de produtos.");
            // Fallback seguro caso o arquivo não exista na primeira execução
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS]; 
            quantosProdutos = 0;
        } finally {
            if (arquivo != null) {
                arquivo.close();
            }
        }
        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime mensagem padrão */
    static void localizarProdutos(){
        int prod = -1;
        cabecalho();
        System.out.println("\nDigite o nome do produto que você gostaria de procurar: ");

        String nome = teclado.nextLine();
        
        // Cria um produto auxiliar apenas para podermos usar o método equals()
        Produto produtoBusca = new ProdutoNaoPerecivel(nome, 1.0);

        for (int i = 0; i < quantosProdutos; i++) {
            // Utiliza o equals() implementado na classe Produto
            if(produtosCadastrados[i].equals(produtoBusca)){
                prod = i;
                break; // Encontrou, pode interromper o laço
            }
        }
        
        if (prod != -1) {
            System.out.println(String.format("%02d - %s", (prod+1), produtosCadastrados[prod].toString()));
        } else {
            System.out.println("Produto não encontrado!");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto(){
        cabecalho();
        Produto produto = null;

        System.out.println("\nO produto que você deseja cadastrar é:");
        System.out.println("1 - Perecível");
        System.out.println("2 - Não Perecível");

        int tipo = Integer.parseInt(teclado.nextLine());

        System.out.println("Qual o nome do produto?");
        String nome = teclado.nextLine();

        System.out.println("Qual o preço do produto?");
        double preco = Double.parseDouble(teclado.nextLine());

        System.out.println("Qual a margem de lucro do produto?");
        double margem = Double.parseDouble(teclado.nextLine());

        if (tipo == 2) {
            produto = new ProdutoNaoPerecivel(nome, preco, margem);
        } else {
            System.out.println("Qual a data de validade? (dd/MM/yyyy)");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataS = teclado.nextLine();
            LocalDate data = LocalDate.parse(dataS, formatter);

            produto = new ProdutoPerecivel(nome, preco, margem, data);
        }

        if (quantosProdutos < produtosCadastrados.length) {
            produtosCadastrados[quantosProdutos] = produto;
            quantosProdutos++;
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Não há espaço para novos produtos.");
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){
        FileWriter arquivo = null;

        try{
            arquivo = new FileWriter((nomeArquivo), Charset.forName("UTF-8"));

            arquivo.append(quantosProdutos + "\n");

            for (int i = 0; i < quantosProdutos; i++) {
                arquivo.append(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }
            arquivo.close();
            System.out.println("Arquivo " + nomeArquivo + " salvo com sucesso.");
        }catch (IOException excecao) {
            System.out.println("Problemas no arquivo " + nomeArquivo + " Tente novamente");
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
