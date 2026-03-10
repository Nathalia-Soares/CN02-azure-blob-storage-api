# Azure Blob Storage + Table Storage API

API REST desenvolvida em Kotlin com Spring Boot para gerenciamento de arquivos no Azure Blob Storage com sistema de logging automático usando Azure Table Storage.

## 📋 Pré-requisitos

- Java 17 ou superior
- Gradle 7.x ou superior
- Conta Azure ativa
- Azure CLI (opcional, para provisionamento automático)

## 🚀 Tecnologias Utilizadas

- **Kotlin** 1.9.22
- **Spring Boot** 3.2.2
- **Azure Storage Blob SDK** 12.25.1
- **Azure Data Tables SDK** 12.3.13
- **SpringDoc OpenAPI** 2.3.0 (Swagger UI)
- **Gradle** (Kotlin DSL)

## 🎯 Funcionalidades

### Azure Blob Storage
- ✅ Upload de imagens
- ✅ Listagem de arquivos com metadados
- ✅ Download de arquivos
- ✅ Visualização no navegador
- ✅ Exclusão de arquivos
- ✅ Informações detalhadas (tamanho, tipo, data)

### Azure Table Storage (LOG)
- ✅ Log automático de todos os uploads
- ✅ Registro de: Nome da foto, Data, Horário e IP
- ✅ Consulta de logs por arquivo
- ✅ Consulta de logs por data
- ✅ Consulta de logs por endereço IP
- ✅ Exclusão de logs
- ✅ Estatísticas de uploads

## 📦 Recursos Azure

### Storage Account
- **Nome**: `eniessi`
- **Container**: `nathcontainer`
- **Table**: `ImageUploadLogs`

### Propriedades do Log
- **nomeDaFoto**: Nome do arquivo de imagem
- **dataDeCadastro**: Data do upload (formato: dd/MM/yyyy)
- **horarioDoCadastro**: Horário do upload (formato: HH:mm:ss)
- **enderecoIp**: Endereço IP da máquina que fez o upload

## 🔧 Provisionamento Rápido

### Opção 1: Script Automatizado (Recomendado)

**Windows (PowerShell):**
```powershell
.\provision-azure-resources.ps1
```

**Linux/Mac (Bash):**
```bash
chmod +x provision-azure-resources.sh
./provision-azure-resources.sh
```

O script irá:
1. ✅ Verificar Azure CLI
2. ✅ Fazer login no Azure
3. ✅ Criar Resource Group
4. ✅ Criar Storage Account
5. ✅ Criar Blob Container
6. ✅ Criar Table Storage
7. ✅ Obter Connection String
8. ✅ Atualizar application.properties automaticamente

### Opção 2: Manual (Portal Azure)

Consulte o guia completo em: **[AZURE_SETUP_GUIDE.md](AZURE_SETUP_GUIDE.md)**

## ⚙️ Configuração

As configurações estão no arquivo `src/main/resources/application.properties`:

```properties
# Azure Blob Storage Configuration
azure.storage.connection-string=<sua-connection-string>
azure.storage.container-name=nathcontainer

# Azure Table Storage Configuration
azure.table.connection-string=<sua-connection-string>
azure.table.name=ImageUploadLogs
azure.table.enabled=true

# Server Configuration
server.port=8080

# Multipart File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**⚠️ IMPORTANTE**: Após executar o script de provisionamento, o arquivo é atualizado automaticamente!

## 🏃 Como Executar

### 1. Build do Projeto
```powershell
# Windows
.\gradlew clean build

# Linux/Mac
./gradlew clean build
```

### 2. Iniciar a Aplicação
```powershell
# Windows
.\gradlew bootRun

# Linux/Mac
./gradlew bootRun
```

A aplicação estará disponível em: `http://localhost:8080`

### 3. Cliente Web HTML

**Interface Completa com Logs:**
Abra o arquivo `client-with-logs.html` no navegador para acessar:
- 📤 Upload de imagens
- 📋 Listagem de blobs
- 📊 Visualização de logs
- 🔍 Busca de logs (por arquivo, data ou IP)

**Interface Simples:**
Use `client.html` para operações básicas de blob storage.

## 📚 Documentação da API (Swagger)

Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Documentação completa e interativa de todos os endpoints!

## 🛠️ Endpoints Disponíveis

### Blob Storage Endpoints

#### 1. Upload de Arquivo
- **POST** `/api/blobs/upload`
- **Content-Type**: `multipart/form-data`
- **Parâmetro**: `file` (arquivo de imagem)
- **Descrição**: Faz upload de uma imagem e registra log automaticamente

**Resposta:**
```json
{
  "fileName": "imagem.jpg",
  "url": "https://eniessi.blob.core.windows.net/nathcontainer/imagem.jpg",
  "message": "Upload realizado com sucesso e log registrado"
}
```

#### 2. Listar Arquivos
- **GET** `/api/blobs/list`
- **Descrição**: Lista todos os arquivos no container

**Resposta:**
```json
[
  {
    "name": "vicky_01.jpg",
    "size": 245678,
    "contentType": "image/jpeg",
    "lastModified": "2026-03-10T14:30:00Z",
    "url": "https://eniessi.blob.core.windows.net/nathcontainer/vicky_01.jpg"
  }
]
```

#### 3. Download de Arquivo
- **GET** `/api/blobs/download/{fileName}`
- **Exemplo**: `/api/blobs/download/vicky_01.jpg`

#### 4. Visualizar Arquivo
- **GET** `/api/blobs/view/{fileName}`
- **Exemplo**: `/api/blobs/view/nathalia.png`

#### 5. Deletar Arquivo
- **DELETE** `/api/blobs/delete/{fileName}`
- **Exemplo**: `/api/blobs/delete/vicky_02.jpg`

### Table Storage (Logs) Endpoints

#### 1. Listar Todos os Logs
- **GET** `/api/logs/list`
- **Descrição**: Lista todos os logs de upload

**Resposta:**
```json
[
  {
    "nomeDaFoto": "vicky_01.jpg",
    "dataDeCadastro": "10/03/2026",
    "horarioDoCadastro": "14:30:45",
    "enderecoIp": "192.168.1.100"
  }
]
```

#### 2. Buscar Log por Nome de Arquivo
- **GET** `/api/logs/file/{fileName}`
- **Exemplo**: `/api/logs/file/vicky_01.jpg`

#### 3. Buscar Logs por Data
- **GET** `/api/logs/date/{date}`
- **Formato da data**: dd/MM/yyyy
- **Exemplo**: `/api/logs/date/10/03/2026`

#### 4. Buscar Logs por IP
- **GET** `/api/logs/ip/{ip}`
- **Exemplo**: `/api/logs/ip/192.168.1.100`

#### 5. Deletar Log
- **DELETE** `/api/logs/delete/{fileName}`
- **Exemplo**: `/api/logs/delete/vicky_01.jpg`

## 📝 Exemplos de Uso

### Upload de arquivo com cURL
```bash
curl -X POST "http://localhost:8080/api/blobs/upload" \
  -F "file=@/caminho/para/imagem.jpg"
```

### Listar todos os logs
```bash
curl -X GET "http://localhost:8080/api/logs/list"
```

### Buscar logs de hoje
```bash
curl -X GET "http://localhost:8080/api/logs/date/10/03/2026"
```

### Buscar logs por IP
```bash
curl -X GET "http://localhost:8080/api/logs/ip/192.168.1.100"
```

## 📂 Estrutura do Projeto

```
azure-blob-storage-api/
├── src/
│   └── main/
│       ├── kotlin/
│       │   └── com/eniessi/azureblobstorageapi/
│       │       ├── azureBlobStorage/
│       │       │   └── config/
│       │       │       ├── AzureBlobStorageConfig.kt
│       │       │       ├── CorsConfig.kt
│       │       │       ├── OpenApiConfig.kt
│       │       │       ├── controller/
│       │       │       │   └── BlobStorageController.kt
│       │       │       ├── model/
│       │       │       │   ├── BlobInfo.kt
│       │       │       │   └── UploadResponse.kt
│       │       │       └── service/
│       │       │           └── AzureBlobStorageService.kt
│       │       ├── azureTableStorage/
│       │       │   ├── config/
│       │       │   │   └── AzureTableStorageConfig.kt
│       │       │   ├── controller/
│       │       │   │   └── TableStorageController.kt
│       │       │   ├── model/
│       │       │   │   └── ImageUploadLog.kt
│       │       │   └── service/
│       │       │       └── AzureTableStorageService.kt
│       │       └── AzureBlobStorageApiApplication.kt
│       └── resources/
│           └── application.properties
├── build.gradle.kts
├── settings.gradle.kts
├── provision-azure-resources.ps1      # Script PowerShell
├── provision-azure-resources.sh       # Script Bash
├── client.html                        # Cliente web simples
├── client-with-logs.html             # Cliente web completo
├── README.md
├── AZURE_SETUP_GUIDE.md              # Guia completo de setup
├── TABLE_STORAGE_README.md           # Documentação Table Storage
├── PROVISION_SCRIPTS_README.md       # Documentação dos scripts
└── SETUP_COMPLETO.md                 # Resumo do setup realizado
```

## 🔒 Segurança

### Boas Práticas

1. **Connection Strings**: 
   - Nunca commite credenciais no repositório
   - Use variáveis de ambiente em produção
   - O arquivo `azure-connection-string.txt` está no `.gitignore`

2. **Azure Key Vault**: 
   - Use Key Vault para armazenar secrets em produção

3. **IP Tracking**: 
   - O sistema captura IPs considerando proxies (X-Forwarded-For, X-Real-IP)

4. **CORS**: 
   - Configurado para aceitar requisições de qualquer origem
   - Ajuste conforme necessário em produção

## 📊 Monitoramento e Logs

### Verificar Logs da Aplicação
A aplicação registra automaticamente:
- Uploads realizados
- Erros de conexão
- Tentativas de acesso

### Verificar no Portal Azure
1. Acesse https://portal.azure.com
2. Navegue até Storage Account → `eniessi`
3. Vá em **Tables** → `ImageUploadLogs`
4. Visualize todos os registros

## 🧹 Limpeza de Recursos

Para deletar todos os recursos Azure criados:

```bash
az group delete --name rg-blob-storage-app --yes --no-wait
```

**⚠️ ATENÇÃO**: Esta ação é irreversível!

## 💰 Custos Estimados

### Azure for Students (Gratuito)
- Primeiros 5 GB de armazenamento: **GRATUITO**
- Primeiras 100k transações/mês: **GRATUITO**
- Table Storage: **GRATUITO** (dentro dos limites)

### Após período gratuito
- Armazenamento LRS: ~R$ 0,04 por GB/mês
- Operações: ~R$ 0,01 por 10.000 transações
- **Uso educacional estimado**: R$ 1-5/mês

## 🆘 Troubleshooting

### Erro: "UnknownHostException"
- Verifique a Connection String
- Verifique conectividade com internet
- Verifique se o Storage Account foi criado

### Erro: "TableClient is not available"
- O Table Storage está configurado como opcional
- A aplicação funciona sem ele (apenas sem logs)
- Verifique `azure.table.enabled=true` no application.properties

### Erro: "Storage account name already taken"
- Escolha outro nome no script de provisionamento
- Modifique a variável `STORAGE_ACCOUNT`

### Aplicação não inicia
```bash
# Limpar e recompilar
.\gradlew clean build --refresh-dependencies

# Verificar porta 8080
netstat -ano | findstr :8080

# Executar com debug
.\gradlew bootRun --debug
```

## 📖 Documentação Adicional

- **[AZURE_SETUP_GUIDE.md](AZURE_SETUP_GUIDE.md)** - Guia completo de provisionamento
- **[TABLE_STORAGE_README.md](TABLE_STORAGE_README.md)** - Documentação detalhada do Table Storage
- **[PROVISION_SCRIPTS_README.md](PROVISION_SCRIPTS_README.md)** - Como usar os scripts
- **[SETUP_COMPLETO.md](SETUP_COMPLETO.md)** - Resumo do setup realizado

## 🎓 Recursos de Aprendizado

- [Azure Storage Documentation](https://docs.microsoft.com/azure/storage/)
- [Azure Table Storage Guide](https://docs.microsoft.com/azure/storage/tables/)
- [Azure CLI Reference](https://docs.microsoft.com/cli/azure/)
- [Spring Boot with Azure](https://spring.io/guides/gs/accessing-data-azure/)

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:
- Reportar bugs
- Sugerir melhorias
- Enviar pull requests

## 👥 Autores

Desenvolvido para o Exercício 02 - Disciplina CN02 - 6º Semestre

## 📄 Licença

Este projeto é de código aberto e está disponível para fins educacionais.

---

**Storage Account**: eniessi  
**Container**: nathcontainer  
**Table**: ImageUploadLogs  
**Data**: 10/03/2026

