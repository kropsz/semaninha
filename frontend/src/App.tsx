import React, { useState } from 'react';
import axios from 'axios';

const CollageGenerator = () => {
  const [username, setUsername] = useState('');
  const [timeRange, setTimeRange] = useState('7day');
  const [gridSize, setGridSize] = useState('3x3');

  const API_URL = 'http://localhost:8080/collage';

  const handleSubmit = async () => {
    const data = {
      username,
      timeRange,
      gridSize,
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
      <div className="content">
      <div className='header'>
        <img src="./assets/icon.svg" alt="icon" />
        <h1>SEMANINHA</h1>
        </div>
        <h3>Descubra sua trilha sonora perfeita com nossa ferramenta de criação de playlists </h3>
        <p>Conecte seu Spotify e deixe nossa ferramenta fazer o trabalho.
          Ela cria playlists personalizadas com suas músicas mais ouvidas recentemente, em segundos.
          Seja para relaxar, treinar ou curtir nostalgia, nós organizamos suas faixas favoritas.

          Começe agora:</p>
      <div className='inputs'>
      <input
        type="text"
        id="username"
        placeholder="LastFm username"
        className=""
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
        <select
          className=""
          value={timeRange}
          onChange={(e) => setTimeRange(e.target.value)}
        >
          <option value="7day">7 day</option>
          <option value="1month">30 day</option>
          <option value="overall">All time</option>
        </select>
        <select
          className=""
          value={gridSize}
          onChange={(e) => setGridSize(e.target.value)}
        >
          <option value="3x3">3x3</option>
          <option value="4x4">4x4</option>
          <option value="5x5">5x5</option>
        </select>
      </div>
      <button
        className=""
        onClick={handleSubmit}
      >
        CREATE COLLAGE
      </button>
      </div>
      <img src="" alt="" />
    </div>
  );
};

export default CollageGenerator;