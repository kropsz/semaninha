import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { LinearProgress } from '@mui/material';
import './home.css';
import Carousel from '../components/corousel';
import women from '../assets/headphone.png';
import icon from '../assets/icon.svg';

const Home = () => {
  const [user, setUser] = useState('');
  const [period, setPeriod] = useState('7day');
  const [limit, setLimit] = useState('9'); 
  const [loading, setLoading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [buffer, setBuffer] = useState(10);
  const navigate = useNavigate();

  const API_URL = 'http://localhost:8080/v1/semaninha/collage';

  useEffect(() => {
    if (loading) {
      const timer = setInterval(() => {
        setProgress((prevProgress) => (prevProgress >= 100 ? 0 : prevProgress + 10));
        setBuffer((prevBuffer) => (prevBuffer >= 100 ? 10 : prevBuffer + 10));
      }, 500);

      return () => {
        clearInterval(timer);
      };
    }
  }, [loading]);

  const handleSubmit = async () => {
    setLoading(true);
    const data = {
      user,
      period,
      limit,
    };

    try {
      const response = await axios.post(API_URL, data);
      console.log('Collage created:', response.data);
      navigate('/nova-pagina'); 
    } catch (error) {
      console.error('Erro ao fazer a requisição:', error);
    } finally {
      setLoading(false);
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
        {!loading && (
          <div className='inputs'>
            <input
              type="text"
              id="user"
              placeholder="lastFm username"
              value={user}
              onChange={(e) => setUser(e.target.value)}
            />
            <select
              value={period}
              onChange={(e) => setPeriod(e.target.value)}
            >
              <option value="7day">7 dias</option>
              <option value="1month">30 dias</option>
              <option value="overall">All time</option>
            </select>
            <select
              value={limit}
              onChange={(e) => setLimit(e.target.value)}
            >
              <option value="9">3x3</option>
              <option value="16">4x4</option>
              <option value="25">5x5</option>
            </select>
          </div>
        )}
        {loading ? (
          <>
          <LinearProgress className='custom-progress-bar' variant="determinate" value={progress} valueBuffer={buffer} />
          <Carousel />
          </>
        ) : (
          <button
            className="btn glow-on-hover"
            onClick={handleSubmit}>
            CRIAR COLLAGEM
          </button>
        )}
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