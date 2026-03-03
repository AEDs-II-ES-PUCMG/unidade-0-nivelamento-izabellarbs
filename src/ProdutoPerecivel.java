
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto{
    private static final double DESCONTO = 0.25;
    private static int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        if (validade.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Este produto está vencido.");
        }
        dataDeValidade = validade;
        
    }
    

    @Override
    public double valorDeVenda () {
        double desconto = 0d;
        int diasValidade = LocalDate.now().until(dataDeValidade).getDays();
        if (diasValidade <= PRAZO_DESCONTO)  {
            desconto = DESCONTO;
        }
        return ( (precoCusto * (1.0 + margemLucro))  * ( 1 - desconto) ); 
       
    }
    
    @Override
    public String toString () {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dados = super.toString();
        dados += "\nVálido até " +formato.format(dataDeValidade);
        return dados;
    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String precoFormatado = String.format ("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format ("%.2f", margemLucro).replace(",", ".");
        String dataFormatada = formato.format(dataDeValidade);
        return String.format("2;%s;%s;%s;%s", descricao, precoFormatado, margemFormatada, dataFormatada);
    }
}
