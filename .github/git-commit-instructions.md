# OBJETIVO
Gerar **mensagens de commit claras, consistentes e úteis** para este repositório.  
As mensagens devem seguir boas práticas:
- Título breve e descritivo (máx. ~50 caracteres).
- Explicação opcional mais detalhada no corpo (quando necessário).
- Devem ser escritas no **imperativo** ("Adicionar", "Corrigir", "Atualizar").
- Estrutura baseada no padrão **Conventional Commits** adaptado para português.

---

# REGRAS DE RESPOSTA
- Perguntar informações adicionais se o contexto não for suficiente.
- Usar **português** para mensagens, salvo se o projeto exigir inglês.
- Nunca gerar mensagens vagas como "update" ou "fix".
- Priorizar clareza em vez de formalidade excessiva.

---

# FORMATO DA MENSAGEM
<tipo>(<escopo>): <resumo curto>
[corpo opcional]
[rodapé opcional]


### Tipos
- **feat:** nova funcionalidade
- **fix:** correção de bug
- **docs:** alterações em documentação
- **style:** mudanças de formatação/estilo (sem impacto em código)
- **refactor:** refatoração de código (sem correção ou feature nova)
- **perf:** melhoria de performance
- **test:** inclusão ou ajuste de testes
- **chore:** tarefas de manutenção (build, dependências, configs)

### Escopo
- Opcional. Pode ser o nome do módulo, pasta ou classe afetada.
- Exemplo: `feat(usuario): adicionar upload de foto`

### Corpo
- Explicar **o que mudou** e **por que mudou** (quando necessário).
- Pode ter múltiplas linhas.

### Rodapé
- Para issues, tarefas ou breaking changes.
- Exemplo: `BREAKING CHANGE: script de migração necessário` ou `Closes #42`.

---

# REGRAS DE FORMATAÇÃO DE SAÍDA
- Usar bullets para listar alternativas de commits se houver mais de uma opção.
- Manter mensagens curtas, diretas e claras.
- Evitar jargões ou abreviações não padrão.

---

# EXEMPLOS DE REFERÊNCIA
feat(usuario): adicionar upload de foto de perfil

Permitir que usuários façam upload de fotos em formato JPEG/PNG.
As imagens são armazenadas no S3 com URL assinado.
Closes #123

fix(pedido): corrigir cálculo de total

Ajustado problema de arredondamento ao aplicar descontos e impostos.
Antes, o valor final podia ficar incorreto por alguns centavos.

docs(readme): atualizar passos de instalação

Incluído passo de migração do banco de dados que estava faltando.
test(pagamento): adicionar testes unitários para método charge

refactor(api): extrair middleware de tratamento de erros

Centralizado o tratamento de erros para reduzir duplicação de código.
 
---

# CHECKLIST PARA COMMITS
Antes de sugerir uma mensagem de commit, validar:
- [ ] O título está curto (máx. 50 caracteres) e claro?
- [ ] O título está no **imperativo** (ex: "Adicionar", "Corrigir", "Remover")?
- [ ] O tipo correto foi usado (`feat`, `fix`, `docs`, `test`, etc.)?
- [ ] Há escopo definido quando necessário (ex: `feat(auth)`)
- [ ] Há corpo/rodapé explicando o "porquê" quando a mudança não é óbvia?  
 