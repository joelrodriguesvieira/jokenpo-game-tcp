package Enums;

public enum RespostaEnum {
    SIM("sim"),
    NAO("não");

    private final String resposta;

    RespostaEnum(String resposta) {
        this.resposta = resposta;
    }

    public String getResposta() {
        return resposta;
    }
}
