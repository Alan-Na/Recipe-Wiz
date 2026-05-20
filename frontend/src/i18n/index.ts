import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import en from './locales/en';
import zh from './locales/zh';

const LANG_STORAGE_KEY = 'recipewiz-lang';

const savedLang = localStorage.getItem(LANG_STORAGE_KEY) ?? 'en';

i18n.use(initReactI18next).init({
  resources: {
    en: { translation: en },
    zh: { translation: zh },
  },
  lng: savedLang,
  fallbackLng: 'en',
  interpolation: {
    escapeValue: false,
  },
});

// Persist language choice whenever it changes
i18n.on('languageChanged', (lng) => {
  localStorage.setItem(LANG_STORAGE_KEY, lng);
});

export { LANG_STORAGE_KEY };
export default i18n;
