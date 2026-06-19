# Minecraft Mod Creator - Especificação do Projeto

## 1. Visão Geral do Projeto

**Nome do Projeto:** MinecraftModCreator  
**Tipo:** Aplicativo Android nativo  
**Resumo:** Aplicativo para criar mods personalizados para Minecraft que permitem aos usuários adicionar novos conteúdos (blocos, itens, ferramentas, mobs, etc.) sem comprometer as conquistas do mundo.  
**Usuários Alvo:** Jogadores de Minecraft que desejam personalizar sua experiência de jogo com mods caseiros.

## 2. Tecnologia & Bibliotecas

### Framework e Linguagem
- **Linguagem:** Kotlin 1.9.x
- **UI Framework:** Jetpack Compose com Material Design 3
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34

### Bibliotecas Principais
- **Compose BOM:** 2024.02.00
- **Navigation:** Compose Navigation 2.7.7
- **ViewModel:** Lifecycle ViewModel Compose 2.7.0
- **Coroutines:** Kotlinx Coroutines 1.8.0
- **Room:** Room Database 2.6.1
- **Hilt:** Dagger Hilt 2.50
- **DataStore:** Preferences DataStore 1.0.0
- **JSON:** Kotlinx Serialization 1.6.2

### Arquitetura
- **Padrão:** MVVM + Clean Architecture
- **DI:** Hilt para injeção de dependências
- **State Management:** StateFlow + Compose State

## 3. Feature List

### 3.1 Tela Inicial (Home)
- Dashboard com estatísticas dos mods criados
- Acesso rápido aos mods recentes
- Botão para criar novo mod

### 3.2 Criador de Mods
- **Criação de Itens:** Nome, ícone, propriedades (dano, resistência, empilhavelidade)
- **Criação de Blocos:** Nome, textura, resistência, ferramenta necessária
- **Criação de Ferramentas:** Tipo (picareta, enxada, etc.), materiais, durabilidade
- **Criação de Armor:** Peças, materiais, proteção
- **Criação de Mobs:** Nome, vida, dano, comportamento (passivo/agressivo)
- **Criação de Receitas:** Crafting de novos itens/blocos

### 3.3 Editor Visual
- Editor WYSIWYG para customização de propriedades
- Preview dos elementos criados
- Validação em tempo real

### 3.4 Gerenciamento de Mods
- Lista de todos os mods criados
- Edição e exclusão de mods
- Duplicar mod existente

### 3.5 Exportação
- Exportar como **Datapack** (compatible com achievements)
- Exportar como **Resource Pack** (texturas e assets)
- Compartilhar mod via arquivo ZIP

### 3.6 Biblioteca de Templates
- Templates pré-definidos para começar rapidamente
- Categorias: Decoração, Utilidade, Aventura, Desafio

## 4. UI/UX Design Direction

### Estilo Visual
- **Design System:** Material Design 3 (Material You)
- **Tema:** Minecraft-inspired com cores terrosas e pixel art
- **Modo Escuro:** Suporte completo

### Paleta de Cores
- **Primária:** Verde esmeralda (#4CAF50) - Reminiscente do grass block
- **Secundária:** Marrom terra (#8D6E63)
- **Ternária:** Azul cielo (#64B5F6)
- **Background:** Areia clara (#F5F5DC) / Fundo escuro (#1E1E1E)
- **Erro:** Vermelho lava (#FF5722)

### Layout
- **Navegação:** Bottom Navigation Bar com 4 abas
  - Home (Dashboard)
  - Criar (Editor)
  - Meus Mods (Lista)
  - Biblioteca (Templates)
- **Cards:** Com cantos arredondados e sombras suaves
- **Ícones:** Estilo outlined com preenchimento ao selecionar

### Interações
- Feedback tátil com Haptic Feedback
- Animações suaves de transição
- Pull-to-refresh em listas
- Snackbar para feedback de ações

## 5. Estrutura de Dados

### Entidades
- **Mod:** id, nome, descrição, versão, dataCriacao, elementos[]
- **ModElement:** id, tipo (ITEM|BLOCO|FERRAMENTA|ARMOR|MOB|RECEITA), propriedades
- **Template:** id, nome, categoria, elementos[]

### Tipos de Elementos Suportados
1. **Item:** nome, icone, empilhavel, quantidadeMax, propriedadesCustom
2. **Bloco:** nome, textura, resistencia, ferramenta, drops
3. **Ferramenta:** tipo, material, durabilidade, velocidade, dano
4. **Armor:** parte, material, protecao, resistencia
5. **Mob:** nome, tipo, vida, dano, comportamento, texto
6. **Receita:** resultado, ingredientes, tipo (crafting/smelting)

## 6. Formato de Exportação

### Datapack (preserva achievements)
```
mod_name/
├── pack.mcmeta
├── data/
│   └── namespace/
│       ├── loot_tables/
│       ├── recipes/
│       ├── advancements/
│       └── tags/
```

### Resource Pack
```
mod_name_resources/
├── pack.mcmeta
├── assets/
│   └── namespace/
│       ├── models/
│       ├── textures/
│       └── lang/
```
