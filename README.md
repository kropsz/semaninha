# 🎶 Semaninha 🎶

O Semaninha é uma aplicação web que permite você criar colagens musicais e playlists com base no seu histórico de músicas do Spotify. Acompanhe o que está ouvindo ao longo da semana, compare com seus amigos e descubra novas músicas baseadas no seu gosto!

## 🖥️ Tecnologias Utilizadas

Aqui estão as principais tecnologias que fazem o **Semaninha** funcionar:

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)

---
> ℹ️ **Observação**: O **Semaninha** também utiliza o [Spotify Web API Java Wrapper](https://github.com/spotify-web-api-java/spotify-web-api-java) para facilitar a integração com a API do Spotify.

### ✨ Funcionalidades

- 🎧 Criação de colagens semanais com suas músicas mais ouvidas.
- 📅 Comparação de histórico musical entre semanas ou com amigos.
- 🎶 Geração de playlists diretamente no Spotify.
- 🤖 Recomendações automáticas de novas músicas baseadas no seu gosto.

<img src="https://github.com/user-attachments/assets/546f27a4-56a6-4484-abb8-f96bf2a3646c" alt="Tela Principal" width="500">
<img src="https://github.com/user-attachments/assets/7a41ec88-2e42-4cbc-b8fd-3e46320141a4" alt="Tela Playlist" width="500">

No lado do servidor, temos três microserviços conectados via **RabbitMQ** para facilitar a comunicação assíncrona entre eles:

- **mslastfm**: Responsável pela criação das colagens musicais.
- **msspotify**: Cuida da criação de playlists.
- **msrecommendation**: Fornece recomendações musicais personalizadas com base nas suas preferências.

<img src="https://github.com/user-attachments/assets/513b1c6c-6b05-478d-ad2a-447107b36fb8" alt="Arquitetura Semaninha" width="500">

Os dados das colagens são salvos em um bucket **S3** da **AWS** e armazenados no **MongoDB**, enquanto as músicas buscadas via API externas são mantidas temporariamente no **Redis** para otimizar o desempenho.

---

## 🚀 Como Executar a Aplicação

### 🐳 Utilizando Docker Compose

Para executar a aplicação usando Docker Compose, siga os passos abaixo:

1. **Certifique-se de ter o Docker e o Docker Compose instalados.** Você pode instalar o Docker seguindo [este guia](https://docs.docker.com/get-docker/) e o Docker Compose seguindo [este guia](https://docs.docker.com/compose/install/).

2. Baixe o arquivo **`docker-compose.yml`**

3. **Edite o arquivo `docker-compose.yml` para ajustar as variáveis de ambiente conforme necessário.** 
   > ⚠️ **Aviso**: As variáveis de ambiente padrão podem não funcionar em seu ambiente. Substitua pelos valores corretos antes de iniciar a aplicação.

4. **Execute o Docker Compose:**

    ```bash
    docker-compose up -d
    ```

   O Docker Compose irá construir e iniciar os containers para todos os serviços da aplicação.

### Utilizando ⚛️ React com npm e ☕ Java com JDK

Para executar a aplicação localmente sem usar Docker, siga os passos abaixo:

1. **Certifique-se de ter o [Node.js](https://nodejs.org/) e o [JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) instalados.** Você pode instalar o Node.js e npm seguindo [este guia](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) e o JDK seguindo [este guia](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html).

2. **Clone o repositório:**

    ```bash
    git clone https://github.com/kropsz/semaninha.git
    cd semaninha
    ```

3. **Instale as dependências do frontend:**

    ```bash
    cd frontend
    npm install
    ```

4. **Inicie o servidor frontend:**

    ```bash
    npm run dev
    ```

5. **Configure e inicie o servidor backend:**

    - **Navegue até o diretório dos microserviços:**

        ```bash
        cd mslastfm
        ```

    - **Compile e execute a aplicação Java:**

        ```bash
        ./mvnw spring-boot:run
        ```

6. > ⚠️ **Aviso**: **Certifique-se de ajustar as variáveis de ambiente necessárias em seus arquivos de configuração.** As configurações padrão podem precisar ser modificadas para corresponder ao seu ambiente local.

---

## 📖 Documentação dos Endpoints

* EM CONSTRUÇÃO

A documentação completa dos endpoints da aplicação pode ser encontrada [aqui no Notion](#).

---

## 🤝 Como Contribuir com o Semaninha

Gostaria de contribuir para o Semaninha? Aqui estão algumas maneiras de ajudar:

1. **Reportar Problemas**: Se você encontrar bugs ou problemas, sinta-se à vontade para abrir um [issue](https://github.com/seu-repositorio/issues) no GitHub. Descreva o problema com o máximo de detalhes possível para que possamos resolver rapidamente.

2. **Sugerir Novas Funcionalidades**: Tem uma ideia para uma nova funcionalidade? Abra uma [issue](https://github.com/seu-repositorio/issues) com suas sugestões ou adicione uma solicitação de pull request (PR) com a implementação da funcionalidade desejada.

3. **Contribuir com Código**: Se você quiser contribuir com melhorias ou correções, faça um fork do repositório, faça suas alterações e envie um [pull request](https://github.com/seu-repositorio/pulls). 

4. **Documentação**: Se você encontrar áreas da documentação que podem ser melhoradas, fique à vontade para enviar uma contribuição com correções ou melhorias na documentação.

5. **Compartilhar**: Se você gosta do Semaninha, compartilhe com seus amigos e colegas para ajudar a espalhar a palavra!

Obrigado por seu interesse e apoio ao Semaninha! Cada contribuição ajuda a melhorar a aplicação e a tornar a experiência musical de todos ainda melhor.

## 👋 Considerações Finais

Esperamos que o **Semaninha** proporcione uma experiência divertida e personalizada na criação de colagens e playlists com base no seu gosto musical. Sinta-se à vontade para explorar a aplicação, compartilhar com amigos e acompanhar sua jornada musical ao longo do tempo!
