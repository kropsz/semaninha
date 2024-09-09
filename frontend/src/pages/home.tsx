import { useState } from 'react';
import axios from 'axios';
import './home.css';
import women from '../assets/headphone.png';
import icon from '../assets/icon.svg';

const Home = () => {
  const [user, setUser] = useState('');
  const [period, setPeriod] = useState('7day');
  const [limit, setLimit] = useState('3x3');

  const API_URL = 'http://localhost:8080/v1/semaninha/collage';

  const handleSubmit = async () => {
    const data = {
      user,
      period,
      limit,
    };

    try {
      const response = await axios.post(API_URL, data);
      console.log('Collage created:', response.data);
    } catch (error) {
      console.error('Erro ao fazer a requisição:', error);
    }
  };

  return (
    <div className="container">
      <div className="background-text top">MUSIC</div>
      <div className="content">
        <div className='header'>
          <img className='icon' src={icon} alt="icon" />
          <h1>SEMANINHA</h1>
        </div>
        <div className='description-container'>
        <h3>Descubra sua trilha sonora perfeita com nossa ferramenta de criação de colagens e playlists </h3>
        <p>Crie colagens de suas músicas mais ouvidas, conecte seu Spotify e deixe nossa ferramenta fazer o trabalho.
          Ela cria playlists personalizadas com suas músicas mais ouvidas recentemente, em segundos.
          Seja para relaxar, treinar ou curtir a nostalgia, nós organizamos suas faixas favoritas.</p>
          </div>
        <div className='inputs'>
          <input
            type="text"
            id="user"
            placeholder="lastFm username"
            className=""
            value={user}
            onChange={(e) => setUser(e.target.value)}
          />
          <select
            className=""
            value={period}
            onChange={(e) => setPeriod(e.target.value)}
          >
            <option value="7day">7 dias</option>
            <option value="1month">30 dias</option>
            <option value="overall">All time</option>
          </select>
          <select
            className=""
            value={limit}
            onChange={(e) => setLimit(e.target.value)}
          >
            <option value="9">3x3</option>
            <option value="16">4x4</option>
            <option value="25">5x5</option>
          </select>
        </div>
        <button
          className="btn glow-on-hover"
          onClick={handleSubmit}>
          CRIAR COLLAGEM
        </button>
        <p>
          Não conhece o LastFm? <a href="https://www.last.fm" target="_blank" rel="noopener noreferrer" className="custom-link">Clique aqui</a>.
        </p>
      </div>
      <div className="background-text bot">NEWWAY</div>  
      <img className="background-image" src={women} alt="headphone" />
    </div>
  );
};

export default Home;