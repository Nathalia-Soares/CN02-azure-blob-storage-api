# Azure Blob Storage API

API REST desenvolvida em Kotlin com Spring Boot para gerenciamento de arquivos no Azure Blob Storage.

## 📋 Pré-requisitos

- Java 17 ou superior
- Gradle 7.x ou superior
- Conexão com Azure Blob Storage

## 🚀 Tecnologias Utilizadas

- **Kotlin** 1.9.22
- **Spring Boot** 3.2.2
- **Azure Storage Blob SDK** 12.25.1
- **SpringDoc OpenAPI** 2.3.0 (Swagger UI)
- **Gradle** (Kotlin DSL)

## 📦 Container Information

- **Container Name**: `nathcontainer`
- **Arquivos existentes**:
  - `vicky_01.jpg`
  - `vicky_02.jpg`
  - `nathalia.png`

## ⚙️ Configuração

As configurações do Azure Blob Storage estão no arquivo `src/main/resources/application.properties`:

```properties
azure.storage.connection-string=<sua-connection-string>
azure.storage.container-name=nathcontainer
```

## 🔧 Como Executar

### Opção 1: Usando Gradle Wrapper Manualmente

**Windows:**
```powershell
# Compilar o projeto
.\gradlew.bat build

# Executar a aplicação
.\gradlew.bat bootRun
```

**Linux/Mac:**
```bash
# Compilar o projeto
./gradlew build

# Executar a aplicação
./gradlew bootRun
```

A aplicação estará disponível em: `http://localhost:8080`

### Opção 2: Cliente Web HTML

Após iniciar a aplicação, abra o arquivo `client.html` no seu navegador para uma interface gráfica amigável!

## 📚 Documentação da API (Swagger)

Após iniciar a aplicação, acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui.html
```

## 🌐 Cliente Web (Interface Gráfica)

O projeto inclui um cliente web HTML (`client.html`) com uma interface amigável para:
- ✅ Upload de imagens com drag-and-drop visual
- ✅ Listagem de arquivos com visualização em cards
- ✅ Preview de imagens
- ✅ Download de arquivos
- ✅ Exclusão de arquivos

**Como usar:**
1. Inicie a aplicação Spring Boot
2. Abra o arquivo `client.html` em qualquer navegador
3. Use a interface gráfica para gerenciar seus arquivos!

## 🛠️ Endpoints Disponíveis

### 1. Upload de Arquivo
- **POST** `/api/blobs/upload`
- **Content-Type**: `multipart/form-data`
- **Parâmetro**: `file` (arquivo de imagem)
- **Descrição**: Faz upload de uma imagem para o Azure Blob Storage

### 2. Listar Arquivos
- **GET** `/api/blobs/list`
- **Descrição**: Lista todos os arquivos no container
- **Retorno**: Array com informações dos blobs (nome, tamanho, tipo, data de modificação, URL)

### 3. Download de Arquivo
- **GET** `/api/blobs/download/{fileName}`
- **Descrição**: Faz download de um arquivo específico
- **Exemplo**: `/api/blobs/download/vicky_01.jpg`

### 4. Visualizar Arquivo
- **GET** `/api/blobs/view/{fileName}`
- **Descrição**: Visualiza um arquivo diretamente no navegador
- **Exemplo**: `/api/blobs/view/nathalia.png`

### 5. Deletar Arquivo
- **DELETE** `/api/blobs/delete/{fileName}`
- **Descrição**: Deleta um arquivo do container
- **Exemplo**: `/api/blobs/delete/vicky_02.jpg`

## 📝 Exemplos de Uso

### Listar todos os arquivos (cURL)
```bash
curl -X GET "http://localhost:8080/api/blobs/list"
```

### Upload de arquivo (cURL)
```bash
curl -X POST "http://localhost:8080/api/blobs/upload" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/caminho/para/sua/imagem.jpg"
```

### Download de arquivo (cURL)
```bash
curl -X GET "http://localhost:8080/api/blobs/download/vicky_01.jpg" \
  --output vicky_01.jpg
```

### Deletar arquivo (cURL)
```bash
curl -X DELETE "http://localhost:8080/api/blobs/delete/vicky_02.jpg"
```

## 📂 Estrutura do Projeto

```
azure-blob-storage-api/
├── src/
│   └── main/
│       ├── kotlin/
│       │   └── com/example/azureblobstorageapi/
│       │       ├── config/
│       │       │   ├── AzureBlobStorageConfig.kt
│       │       │   └── OpenApiConfig.kt
│       │       ├── controller/
│       │       │   └── BlobStorageController.kt
│       │       ├── model/
│       │       │   ├── BlobInfo.kt
│       │       │   └── UploadResponse.kt
│       │       ├── service/
│       │       │   └── AzureBlobStorageService.kt
│       │       └── AzureBlobStorageApiApplication.kt
│       └── resources/
│           └── application.properties
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🔒 Segurança

**IMPORTANTE**: A connection string contém credenciais sensíveis. Em produção:
- Use Azure Key Vault para armazenar secrets
- Configure variáveis de ambiente
- Nunca commite credenciais no repositório

## 🤝 Contribuindo

Sinta-se à vontade para contribuir com melhorias!

## 📄 Licença

Este projeto é de código aberto e está disponível para fins educacionais.