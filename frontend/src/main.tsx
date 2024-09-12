import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './index.css';
import Home from './pages/home/home';
import PlaylistComponent from './pages/playlist/playlist';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<PlaylistComponent />} />
        <Route path="/nova-pagina" element={<Home />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
);