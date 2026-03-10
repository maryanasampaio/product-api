# Simulacao de Vendas no Cartao - Integracao Frontend

## Objetivo
Implementar o fluxo de simulacao seguindo a logica InfinityPay (modelo de repasse por divisao), com contrato pronto para consumo no frontend.

## Status
- Backend implementado e validado.
- Rotas testadas com sucesso em `http://localhost:3000`.
- Tabela de taxas configurada conforme solicitado (1x a 12x).

## Endpoints

### 1) Listar bandeiras ativas
- `GET /card-brands`
- `GET /api/card-brands`

Resposta:
```json
[
  { "id": "visa", "name": "Visa" },
  { "id": "mastercard", "name": "Mastercard" },
  { "id": "elo", "name": "Elo" },
  { "id": "amex", "name": "American Express" },
  { "id": "hipercard", "name": "Hipercard" }
]
```

### 2) Calcular simulacao
- `POST /simulations/calculate`
- `POST /api/simulations/calculate`

Request body:
```json
{
  "amount": 1000.0,
  "installments": 2,
  "card_brand": "visa",
  "repasse": true
}
```

Campos de entrada:
- `amount`: number > 0
- `installments`: integer entre 1 e 12
- `card_brand`: string (`visa`, `mastercard`, `elo`, `amex`, `hipercard`)
- `repasse`: boolean

Response body (objeto direto, sem envelope):
```json
{
  "repasse": true,
  "installments": 2,
  "base_amount": 1000.00,
  "final_amount": 1064.85,
  "installment_value": 532.42,
  "interest_total": 64.85,
  "operator_fee": 64.85,
  "net_amount": 1000.00,
  "card_brand": "visa"
}
```

## Regras de calculo (InfinityPay)

### Com repasse (`repasse = true`)
- `taxa = rate_percent / 100`
- `final_amount = amount / (1 - taxa)`
- `installment_value = final_amount / installments`
- `interest_total = final_amount - amount`
- `operator_fee = final_amount - amount`
- `net_amount = amount`

### Sem repasse (`repasse = false`)
- `taxa = rate_percent / 100`
- `final_amount = amount`
- `installment_value = amount / installments`
- `operator_fee = amount * taxa`
- `net_amount = amount - operator_fee`
- `interest_total = 0`

Arredondamento:
- Todos os valores monetarios retornam com 2 casas decimais (`HALF_UP`).

## Tabela de taxas aplicada
- 1x: 4.20%
- 2x: 6.09%
- 3x: 7.01%
- 4x: 7.91%
- 5x: 8.80%
- 6x: 9.67%
- 7x: 12.59%
- 8x: 13.42%
- 9x: 14.25%
- 10x: 15.06%
- 11x: 15.87%
- 12x: 16.66%

## Exemplos validados

### Exemplo A - com repasse (2x, 6.09%)
Entrada:
```json
{
  "amount": 1000.0,
  "installments": 2,
  "card_brand": "visa",
  "repasse": true
}
```
Saida:
```json
{
  "base_amount": 1000.00,
  "final_amount": 1064.85,
  "installment_value": 532.42,
  "interest_total": 64.85,
  "operator_fee": 64.85,
  "net_amount": 1000.00,
  "repasse": true,
  "card_brand": "visa",
  "installments": 2
}
```

### Exemplo B - sem repasse (2x, 6.09%)
Entrada:
```json
{
  "amount": 1000.0,
  "installments": 2,
  "card_brand": "visa",
  "repasse": false
}
```
Saida:
```json
{
  "base_amount": 1000.00,
  "final_amount": 1000.00,
  "installment_value": 500.00,
  "interest_total": 0.00,
  "operator_fee": 60.90,
  "net_amount": 939.10,
  "repasse": false,
  "card_brand": "visa",
  "installments": 2
}
```

## Erros esperados
- Bandeira inexistente/inativa:
  - `404` com mensagem: `RATE_NOT_FOUND: card_brand not found or inactive`
- Taxa nao configurada para cenario:
  - `404` com mensagem: `RATE_NOT_FOUND: rate not configured for this scenario`
- Payload invalido:
  - `400` com erros de validacao (`amount`, `installments`, `card_brand`, `repasse`)

## Integracao Angular (resumo)

Service:
```typescript
calculate(input: { amount: number; installments: number; card_brand: string; repasse: boolean }) {
  return this.http.post<SimulationResponse>(`${apiUrl}/simulations/calculate`, input);
}

getCardBrands() {
  return this.http.get<CardBrand[]>(`${apiUrl}/card-brands`);
}
```

Interfaces:
```typescript
export interface SimulationResponse {
  base_amount: number;
  final_amount: number;
  installment_value: number;
  interest_total: number;
  operator_fee: number;
  net_amount: number;
  repasse: boolean;
  card_brand: string;
  installments: number;
}

export interface CardBrand {
  id: string;
  name: string;
}
```

## Observacoes de operacao
- Seed automatico garante bandeiras e taxas no startup.
- Caso banco esteja sem dados, reiniciar app para semear novamente.
- Porta atual de validacao: `3000`.
