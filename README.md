# Recipe Wiz

**English** | [中文](./README.zh.md)

## Project Overview

Recipe Wiz is a full-stack meal planning and recipe management web application. It helps users discover recipes by ingredient, analyze nutrition, plan a weekly menu, and now generate AI-powered meal plans — all from a modern, bilingual (English / Chinese) interface.

## Contents

1. [Author](#author)
2. [Key Features](#key-features)
3. [Tech Stack](#tech-stack)
4. [Getting Started](#getting-started)
5. [Usage Guide](#usage-guide)
6. [License](#license)
7. [Support and Feedback](#support-and-feedback)
8. [How to Contribute](#how-to-contribute)
9. [API Usage](#api-usage)

## Author

- Xineng Na (@Alan-Na)

## Key Features

- **Smart Recipe Search**
  - Find recipes using ingredients you have on hand.
  - Filter by diet labels, health labels, and cuisine types.
  - Results persist across page navigation — no accidental refresh.

- **Nutrition Analysis**
  - Per-recipe detailed nutrient breakdown (calories, protein, fat, carbs, vitamins, minerals).
  - Color-coded nutrient cards for quick scanning.

- **Personalized Meal Planner**
  - Weekly calendar with day-by-day meal slots (Breakfast / Lunch / Dinner / Snack).
  - Daily calorie total shown for each day that has meals planned.
  - Update meal status (Planned → In Progress → Completed).
  - Add, remove, and re-schedule meals freely.

- **Saved Recipes Library**
  - Save any searched recipe with one click.
  - Delete saved recipes directly from the Meal Planner panel.
  - Saved recipes feed directly into the weekly calendar.

- **AI Meal Planner** *(powered by DeepSeek)*
  - Enter your gender, age, height, weight, and health goal.
  - AI generates a complete week-to-Sunday meal plan using only your saved recipes.
  - Preview the plan before committing — regenerate if needed, then confirm to save all entries at once.
  - Requires at least 3 saved recipes to activate.

- **Adjustable Serving Sizes**
  - Scale any recipe up or down; ingredient quantities recalculate automatically.

- **Bilingual UI**
  - Full English / Chinese support with instant runtime toggle (persists across sessions).

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 19, TypeScript, Vite, Chakra UI v2, TanStack Query v5, framer-motion, react-i18next |
| Backend | Spring Boot 3.2, Java 17, Maven, Clean Architecture |
| Database | SQLite (via JDBC) |
| External APIs | Edamam Recipe Search API, Edamam Nutrition Analysis API, DeepSeek Chat API |

## Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.8+

### 1 — Clone

```bash
git clone https://github.com/Alan-Na/Recipe-Wiz.git
cd Recipe-Wiz
```

### 2 — Backend

```bash
cd backend
mvn package -DskipTests
java -jar target/recipewiz-backend-0.1.0-SNAPSHOT.jar --server.port=9271
```

> The server starts on port **9271** by default. Override with `--server.port=<port>`.

### 3 — Frontend

```bash
cd frontend
cp .env.example .env.local          # or create manually (see below)
npm install
npm run dev -- --port 9272
```

`.env.local` should contain:

```
VITE_API_BASE_URL=http://localhost:9271
```

The app will be available at **http://localhost:9272**.

### Environment Variables (optional overrides)

| Variable | Default | Description |
|---|---|---|
| `RECIPE_APP_ID` | bundled | Edamam Recipe Search app ID |
| `RECIPE_APP_KEY` | bundled | Edamam Recipe Search app key |
| `NA_APP_ID` | bundled | Edamam Nutrition Analysis app ID |
| `NA_APP_KEY` | bundled | Edamam Nutrition Analysis app key |
| `DEEPSEEK_API_KEY` | bundled | DeepSeek API key for AI Meal Planner |

## Usage Guide

### Recipe Search

1. **Add ingredients** — type an ingredient name in the input field and press **Add** or hit Enter.
2. **Apply filters** — click **Filters** to open the restriction drawer and select diet / health / cuisine labels. Active filters appear as removable tags.
3. **Search** — click **Search Recipes**. Results stay on screen if you navigate away and come back.
4. **Analyze nutrition** — click **Analyze Nutrition** on any recipe card to view the full nutrient breakdown.
5. **Save a recipe** — click **Save Recipe** to add it to your library (used by the Meal Planner).
6. **Adjust servings** — enter a number and click **Update Servings** to rescale ingredient quantities.
7. **Clear results** — use the **Clear Results** button (top-right of the page header) to reset everything manually.

### Meal Planner

1. **Navigate weeks** — use the `←` / `→` arrows or click **Today** to jump to the current week.
2. **Add a meal** — click **Add to Calendar** on any saved recipe, then choose the day and meal type.
3. **Daily calorie total** — each day column shows a 🔥 calorie badge (sum of all meals that day) whenever at least one meal is planned.
4. **Update status** — use the dropdown on each meal entry to mark it as Planned / In Progress / Completed.
5. **Remove a meal** — click the trash icon on any meal entry.
6. **Delete a saved recipe** — click the 🗑️ icon on a recipe card in the Saved Recipes panel.

### AI Meal Planner

1. On the Meal Planner page, expand the **🤖 AI Meal Planner** panel.
2. Select your **gender**, **age**, **height**, and **weight** from the dropdowns.
3. Describe your **health goal** (e.g. "lose weight", "high protein", "low carb").
4. Click **Generate Meal Plan** — the AI builds a plan from today through Sunday using your saved recipes.
5. Review the preview. Click **Regenerate** for a different plan or **Confirm & Save to Calendar** to write all entries at once.

> ⚠️ At least **3 saved recipes** are required before the AI planner can be activated.

## License

This project is licensed under the **MIT License** — you are free to use, modify, and distribute the code for personal or commercial purposes, provided the original copyright notice is retained.

See the [LICENSE](./LICENSE) file for the full text.

## Support and Feedback

1. **GitHub Issues** — report bugs or request features at [Issues](https://github.com/Alan-Na/Recipe-Wiz/issues).
2. **Discussions** — join community conversations at [Discussions](https://github.com/Alan-Na/Recipe-Wiz/discussions).

**When reporting a bug, please include:**
- Clear description of the problem.
- Steps to reproduce.
- Relevant screenshots or logs.

## How to Contribute

1. Fork the repository.
2. Clone your fork: `git clone https://github.com/Alan-Na/Recipe-Wiz.git`
3. Create a feature branch.
4. Implement your changes with meaningful commits.
5. Push and open a Pull Request with a clear description.

## API Usage

### Edamam Recipe Search API

Used to search recipes by ingredient, diet label, health label, and cuisine type. Returns recipe titles, ingredient lists, preparation instructions, serving size, and per-serving nutrition data.

→ [Documentation](https://developer.edamam.com/edamam-recipe-api)

### Edamam Nutrition Analysis API

Used to calculate detailed per-nutrient breakdowns (calories, macros, vitamins, minerals) for any recipe, displayed in the Nutrition Analysis modal.

→ [Documentation](https://developer.edamam.com/edamam-nutrition-api)

### DeepSeek Chat API

Used by the AI Meal Planner feature. Receives the user's physical profile, health goal, and list of saved recipes, then returns a structured JSON meal plan covering the remaining days of the current week.

→ [Documentation](https://platform.deepseek.com/api-docs)
