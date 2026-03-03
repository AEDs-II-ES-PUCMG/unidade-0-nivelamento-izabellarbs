import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {

	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;

	/**
	 * Inicializador privado. Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00, 0.0
	 * 
	 * @param desc        Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto  Preço do produto (mínimo 0.01)
	 * @param margemLucro Margem de lucro (mínimo 0.01)
	 */
	protected void init(String desc, double precoCusto, double margemLucro) {

		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}

	/**
	 * Construtor completo. Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00, 0.0
	 * 
	 * @param desc        Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto  Preço do produto (mínimo 0.01)
	 * @param margemLucro Margem de lucro (mínimo 0.01)
	 */
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}

	/**
	 * Construtor sem margem de lucro - fica considerado o valor padrão de margem de
	 * lucro.
	 * Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00
	 * 
	 * @param desc       Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto Preço do produto (mínimo 0.01)
	 */
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}

	/**
	 * Retorna o valor de venda do produto, considerando seu preço de custo e margem
	 * de lucro.
	 * 
	 * @return Valor de venda do produto (double, positivo)
	 */
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}

	/**
	 * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
	 * 
	 * @return String com o formato:
	 *         [NOME]: R$ [VALOR DE VENDA]
	 */
	@Override
	public String toString() {

		NumberFormat moeda = NumberFormat.getCurrencyInstance();

		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}

	/**
	 * Igualdade de produtos: caso possuam o mesmo nome/descrição.
	 * 
	 * @param obj Outro produto a ser comparado
	 * @return booleano true/false conforme o parâmetro possua a descrição igual ou
	 *         não a este produto.
	 */
	@Override
	public boolean equals(Object obj) {
		Produto outro = (Produto) obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	static Produto criarDoTexto (String linha){
		Produto novoProduto = null;
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String[] atributos = linha.split(";");
		int tipo = Integer.parseInt(atributos[0]);
		String descricao = atributos[1];
		double preco = Double.parseDouble(atributos[2]);
		double margem = Double.parseDouble(atributos[3]);
		if (tipo==1){
			novoProduto = new ProdutoNaoPerecivel(descricao, preco, margem);
		}
		else {
			LocalDate datavalidade = LocalDate.parse(atributos[4], formatoData);
			novoProduto = new ProdutoPerecivel(descricao, preco, margem, datavalidade);
		}
		return novoProduto;
	}

	public abstract String gerarDadosTexto();
	

	

}