# CSVRE API

Spring Boot 3.3.9 後端 REST API，提供 ESG 相關數據的查詢與管理服務。

## 技術棧

- **Java 17**
- **Spring Boot 3.3.9**
- **Spring Data JPA** + H2 In-Memory Database
- **Spring Security** (Basic Auth + custom header filter)
- **Lombok**

## 快速啟動

```bash
./mvnw spring-boot:run
```

服務預設運行於 `http://localhost:8080`

## 認證方式

所有 API 需帶入以下認證：

| 類型 | 說明 |
|------|------|
| Basic Auth | username: `admin` / password: `admin123` |
| Header | `login-user: <user-id>`（必填，缺少會回傳 400） |

### curl 範例

```bash
curl -u admin:admin123 -H "login-user: user01" http://localhost:8080/api/emission
```

## API 列表

### Fab（廠區）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/fab` | 取得所有廠區 |

### Power Consumption（用電量）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/power-consumption` | 取得所有用電量資料 |
| POST | `/api/power-consumption` | 新增用電量 |
| PUT | `/api/power-consumption/{id}` | 更新用電量 |

### RE Usage（再生能源使用量）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/re-usage` | 取得所有 RE 使用量 |
| POST | `/api/re-usage` | 新增 RE 使用量 |
| PUT | `/api/re-usage/{id}` | 更新 RE 使用量 |

### Emission（排放量）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/emission` | 取得所有排放量 |
| POST | `/api/emission` | 新增排放量 |
| PUT | `/api/emission/{id}` | 更新排放量 |
| GET | `/api/emission/goal` | 取得排放目標 |
| POST | `/api/emission/goal` | 新增排放目標 |
| PUT | `/api/emission/goal/{id}` | 更新排放目標 |

### Power Factor（電力係數）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/power-factor` | 取得所有電力係數 |
| POST | `/api/power-factor` | 新增電力係數 |
| PUT | `/api/power-factor/{id}` | 更新電力係數 |

### RE Goal（再生能源目標）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/re-goal` | 取得所有 RE 目標 |
| POST | `/api/re-goal` | 新增 RE 目標 |
| PUT | `/api/re-goal/{id}` | 更新 RE 目標 |

### RE Procurement（再生能源採購）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/re-procurement` | 取得所有 RE 採購資料 |
| POST | `/api/re-procurement` | 新增 RE 採購 |
| PUT | `/api/re-procurement/{id}` | 更新 RE 採購 |

### Customer（客戶）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/customer` | 取得所有客戶（來自外部 API） |

### Customer Card（客戶卡）

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/customer-card` | 取得所有客戶卡 |
| GET | `/api/customer-card/{custCode}` | 依客戶代碼取得客戶卡 |
| POST | `/api/customer-card` | 新增客戶卡 |
| PUT | `/api/customer-card/{custCode}` | 更新客戶卡 |

## Customer Card 請求範例

### POST `/api/customer-card`

```json
{
  "custCode": "C001",
  "participateSinceYear": 2022,
  "calenderSinceMonth": 1,
  "calenderToMonth": 12,
  "toThirdParties": [
    { "custCode": "C002", "revenue": 500000.00 }
  ],
  "fromThirdParties": [
    { "custCode": "C003", "revenue": 300000.00 }
  ],
  "participateOptions": [1, 2],
  "rePreference": {
    "TW": {
      "UNBUNDLE": [
        { "type": "WIND", "priority": 1 },
        { "type": "SOLAR", "priority": 2 }
      ]
    }
  },
  "expected": {
    "option": 1,
    "years": [
      { "year": 2024, "rate": 30.00, "isShow": true },
      { "year": 2025, "rate": 50.00, "isShow": true }
    ]
  },
  "commitment": {
    "option": 2,
    "years": [
      { "year": 2024, "rate": 25.00, "isShow": false }
    ]
  }
}
```

## 資料庫

開發環境使用 H2 In-Memory Database，可透過瀏覽器查看：

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: （空白）

## 專案結構

```
src/main/java/com/example/esgdp/csvreapi/
├── config/          # SecurityConfig, AppConfig
├── controller/      # REST Controllers
├── dto/             # Data Transfer Objects
├── exception/       # GlobalExceptionHandler, ErrorResponse
├── filter/          # LoginUserFilter
├── model/           # JPA Entities & Enums
├── repository/      # Spring Data JPA Repositories
└── service/         # Business Logic
```

## Enum 定義

| Enum | 值 |
|------|----|
| `EnergyType` | `WIND`, `SOLAR`, `HYDRO`, `BIO` |
| `DirectionType` | `TO`, `FROM` |
| `PlanType` | `EXPECTED`, `COMMITMENT` |
