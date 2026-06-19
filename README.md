# 🎮 Minecraft Mod Creator

Crie mods para Minecraft sem perder suas conquistas!

## 📱 Descrição

O **Minecraft Mod Creator** é um aplicativo Android que permite criar mods personalizados para Minecraft Bedrock Edition de forma fácil e intuitiva. Os mods são exportados como **Datapacks**, garantindo que as conquistas do seu mundo sejam preservadas!

## ✨ Funcionalidades

- 📦 **Criar Itens** - Itens customizados com propriedades únicas
- 🧱 **Criar Blocos** - Blocos decorativos e funcionais
- ⛏️ **Criar Ferramentas** - Picaretas, espadas, machados e muito mais
- 🛡️ **Criar Armor** - Conjuntos de armaduras protetoras
- 🐱 **Criar Mobs** - Criaturas passivas ou hostis
- 🍖 **Criar Receitas** - Receitas de crafting e smelting

## 📤 Exportação

- **Datapack** - Preserva conquistas do mundo! (Recomendado)
- **Resource Pack** - Adiciona texturas e modelos customizados
- **Compartilhamento** - Exporte como arquivo ZIP para compartilhar

## 🏗️ Tecnologias

- **Kotlin** - Linguagem moderna para Android
- **Jetpack Compose** - UI moderna e responsiva
- **Material Design 3** - Design bonito e consistente
- **Room Database** - Persistência local eficiente
- **Hilt** - Injeção de dependências
- **MVVM + Clean Architecture** - Arquitetura escalável

## 🚀 Como Compilar

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17 ou superior
- Android SDK 34

### Passos

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Aguarde a sincronização do Gradle
4. Conecte um dispositivo ou emulador
5. Clique em "Run" (Shift + F10)

### Build via linha de comando

```bash
./gradlew assembleDebug
```

O APK será gerado em: `app/build/outputs/apk/debug/app-debug.apk`

## 🎨 Templates

O app inclui templates pré-definidos para começar rapidamente:

- **Decorative Lights** - Blocos de luz coloridos
- **Super Tools** - Ferramentas ultimeis
- **Gourmet Food Pack** - Comidas especiais
- **Epic Bosses** - Mobs desafiadores
- **Enchanted Armor Set** - Armaduras mágicas
- **Extra Ores** - Minérios raros

## 📂 Estrutura do Projeto

```
app/src/main/java/com/minecraftmodcreator/
├── data/           # Repositórios e banco de dados
│   ├── local/      # Room database
│   └── repository/ # Implementações de repositórios
├── domain/         # Modelos de domínio
│   └── model/      # Entidades de negócio
├── di/             # Módulos Hilt
└── ui/             # Interface do usuário
    ├── navigation/  # Navegação Compose
    ├── screens/    # Telas do app
    └── theme/      # Tema Material 3
```

## 🐛 Problemas Conhecidos

Se o app fechar ao abrir, tente:

1. Desinstalar e reinstallar o app
2. Limpar dados do app nas configurações
3. Verificar se o Android é 8.0 (API 26) ou superior
4. Verificar se há espaço suficiente no dispositivo

## 📄 Licença

Este projeto é distribuído sob a licença MIT.

## 🤝 Contribuições

Contribuições são bem-vindas! Abra uma issue ou pull request.

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
