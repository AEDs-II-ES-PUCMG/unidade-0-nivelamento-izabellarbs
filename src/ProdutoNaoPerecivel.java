public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
        super(desc, precoCusto, margemLucro);
        
    }
    public ProdutoNaoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);
        
    }
    
    @Override
    public double valorDeVenda() {
        return (precoCusto * (1.0 + margemLucro));
    }


     @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format("%.2f", margemLucro).replace(",", ".");
        if (quantEstoque > 0) {
            return String.format("1;%s;%s;%s;%d", descricao, precoFormatado, margemFormatada, quantEstoque);
        }
        return String.format("1;%s;%s;%s", descricao, precoFormatado, margemFormatada);
    }
     

}
