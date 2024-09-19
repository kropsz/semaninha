import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './index.css';
import Home from './pages/home/home';
import PlaylistComponent from './pages/playlist/playlist';
import CollageCatalog from './components/catalog/catalog';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/playlist" element={<PlaylistComponent />} />
        <Route path="/catalog" element={<CollageCatalog />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
);