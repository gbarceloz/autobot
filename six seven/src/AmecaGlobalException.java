class AmeacaGlobalException extends Exception {
    public AmeacaGlobalException (String mensagem){
        super(mensagem); }
    public AmeacaGlobalException(String mensagem, Throwable causa){
        super(mensagem, causa);}
}
