# Integracao Frontend - Simulacao de Vendas no Cartao

## Visao geral

Foi implementado no backend o fluxo de simulacao com a logica InfinityPay para repasse por divisao:

- Com repasse: `valor_cliente = valor_liquido_desejado / (1 - taxa)`
- Sem repasse: cliente paga o valor base e a loja absorve a taxa.

## Endpoints

### 1. Calcular simulacao

- `POST /simulations/calculate`
- Alias: `POST /api/simulations/calculate`

Request:

```json
{
  "amount": 1000.0,
  "installments": 2,
  "card_brand": "visa",
  "repasse": true
}
```

Response (objeto direto, sem envelope):

```json
{
  "base_amount": 1000.00,
  "final_amount": 1064.86,
  "installment_value": 532.43,
  "interest_total": 64.86,
  "operator_fee": 64.86,
  "net_amount": 1000.00,
  "repasse": true,
  "card_brand": "visa",
  "installments": 2
}
```

### 2. Listar bandeiras ativas

- `GET /card-brands`
- Alias: `GET /api/card-brands`

Response:

```json
[
  { "id": "amex", "name": "American Express" },
  { "id": "elo", "name": "Elo" },
  { "id": "hipercard", "name": "Hipercard" },
  { "id": "mastercard", "name": "Mastercard" },
  { "id": "visa", "name": "Visa" }
]
```

## Regras de calculo

### Com repasse (`repasse=true`)

- `taxa = rate_percent / 100`
- `final_amount = amount / (1 - taxa)`
- `installment_value = final_amount / installments`
- `interest_total = final_amount - amount`
- `operator_fee = final_amount - amount`
- `net_amount = amount`

### Sem repasse (`repasse=false`)

- `taxa = rate_percent / 100`
- `final_amount = amount`
- `installment_value = amount / installments`
- `operator_fee = amount * taxa`
- `net_amount = amount - operator_fee`
- `interest_total = 0`

### Arredondamento

Todos os campos monetarios retornam com 2 casas decimais (HALF_UP).

## Tabela de taxas aplicada

Taxas por parcela (para bandeiras ativas), em ambos cenarios `repasse=true/false`:

- `1x` 4.20%
- `2x` 6.09%
- `3x` 7.01%
- `4x` 7.91%
- `5x` 8.80%
- `6x` 9.67%
- `7x` 12.59%
- `8x` 13.42%
- `9x` 14.25%
- `10x` 15.06%
- `11x` 15.87%
- `12x` 16.66%

## Validacoes

- `amount` obrigatorio e maior que zero
- `installments` inteiro entre 1 e 12
- `card_brand` obrigatoria e ativa
- `repasse` obrigatorio
- combinacao (`card_brand`, `installments`, `repasse`) deve existir
- `rate_percent < 100`

## Erros esperados

### 400 - validacao

Exemplo:

```json
{
  "timestamp": "2026-03-06T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validacao",
  "path": "/simulations/calculate",
  "errors": [
    { "field": "amount", "message": "amount must be greater than zero", "rejectedValue": 0 }
  ]
}
```

### 404 - taxa nao encontrada

Exemplo:

```json
{
  "error": "RATE_NOT_FOUND: rate not configured for this scenario",
  "statusCode": 404
}
```

## Integracao no frontend

No frontend atual:

1. Trocar `useMockData = false` no service de simulacao.
2. Chamar `POST /simulations/calculate` para cada cenario.
3. Para comparacao de cenario:
- chamada 1: `repasse=true`
- chamada 2: `repasse=false`

## Exemplo em Angular

```ts
calculate(payload: {
  amount: number;
  installments: number;
  card_brand: string;
  repasse: boolean;
}) {
  return this.http.post<SimulationResponse>(
    `${environment.apiUrl}/simulations/calculate`,
    payload
  );
}
```

## cURL rapido

```bash
curl -X POST http://localhost:8080/simulations/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1000.0,
    "installments": 2,
    "card_brand": "visa",
    "repasse": true
  }'
```

## Observacoes

- Os endpoints de simulacao nao exigem token por padrao.
- Se desejar proteger tambem com JWT, posso aplicar no `WebMvcAuthConfig`.
