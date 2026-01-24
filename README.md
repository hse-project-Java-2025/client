# SmartCalendarClient

## 📱 Description

**SmartCalendar** — Android-приложение для управления личными и совместными задачами с поддержкой приглашений, статистики и AI-функций. Приложение позволяет легко планировать день, отслеживать прогресс, взаимодействовать с другими пользователями и использовать интеллектуальные подсказки.

## 🔗 Links

Back-end часть проекта доступна по ссылке:

- [SmartCalendarServer](https://github.com/hse-project-Java-2025/server)

# Key Features

## 👤 User Authentication & Profile

- Sign-up, login and credential changes via secure JWT tokens  
- Fetch and display user info (username, email)

## 📅 Task & Daily Schedule Management

- Create, edit, delete and complete tasks with Compose UI 
- Prevents nested-task conflicts with real-time checks

## 🤝 Shared Event Invitations

- Invite users by username or email to collaborate on events  
- Swipe-to-accept or decline invites in the invitations screen  

## 🔄 Background Sync with WorkManager

- TaskApiWorker and InviteApiWorker for reliable offline-first operations  
- Chaining: send invites only after task creation succeeds  
- Unique work policies to prevent duplicate requests

## 📊 Statistics & Progress Tracking

- Display today’s, week’s and total task durations  
- Continuous success days and average time calculations  
- Reactive UI via StatisticsStore and StatisticsViewModel

## 🎙️ Audio Integration

- Record or pick audio clips as voice notes for tasks  
- Upload via AudioRepository and display AI-generated suggestions

## 📐 Modern UI & Navigation

- Jetpack Compose screens and components (cards, swipe, badges)  
- Centralized app nested navigation using common topBar/appWidget 

## 🛠️ Modularity

- Clear separation: Repositories, Stores, ViewModels, UI components

## ⚙️ Dependencies

- Android SDK 34+
- Jetpack Compose
- WorkManager
- Retrofit2+OkHTTP + Moshi
- Kotlinx Serialization
- JUnit 4+5 + MockK

## Quick Start
1. Clone repository:
   ```bash
   git clone https://github.com/hse-project-Java-2025/server.git
   cd smartcalendar-server
   ```
2. Set environment variables (create `.env` file):
   ```ini
   JWT_SECRET=your_strong_secret_here
   CHATGPT_API_KEY=your_openai_api_key
   ```
## License
MIT License - see [LICENSE](LICENSE.txt) file
---

## Contributors
- [Pavel Usatov](https://github.com/UsatovPavel)
- [Timofey Ustinov](https://github.com/timustinov)


AI-функционал
https://github.com/user-attachments/assets/963f8ed1-c95e-4326-ae8b-bdfbd8c01e74
Основной функционал
https://drive.google.com/file/d/1Zq_mHprqxPhZRkU3m5l3qvVPfQEbaJFy/view?usp=sharing