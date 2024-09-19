import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './catalog.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { ChevronLeft } from 'lucide-react';

interface Track {
  name: string;
  artist: {
    name: string;
  };
}

interface Collage {
  imageUrl: string;
  date: string;
  period: string;
  tracks: Track[];
}

const CollageCatalog: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = location.state || {};
  const [collages, setCollages] = useState<Collage[]>([]);
  const [expandedIndex, setExpandedIndex] = useState<number | null>(null);

  const handleBackClick = () => {
    navigate('/playlist');
  };

  useEffect(() => {
    if (user) {
      axios.get(`http://localhost:8080/v1/semaninha/collage/${user}`)
        .then(response => {
          setCollages(response.data);
        })
        .catch(error => {
          console.error('Error fetching collages:', error);
        });
    }
  }, [user]);

  const toggleExpand = (index: number) => {
    setExpandedIndex(expandedIndex === index ? null : index);
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = String(date.getFullYear()).slice(-2);
    return `${day}/${month}/${year}`;
  };

  const translatePeriod = (period: string): string => {
    switch (period) {
      case '7day':
        return '7 dias';
      case '1month':
        return '1 mês';
      case 'overall':
        return 'Geral';
      default:
        return period;
    }
  };

  return (
    <div className='bodyCards'>
      <div className='headers'>
        <h1>Collagens de {user}</h1>
        <button onClick={handleBackClick} className='backButton'>
          <ChevronLeft className='back'/>
        </button>
      </div>
      <div className="catalog-container">
        <div className="catalog">
          {collages.map((collage, index) => (
            <div key={index} className="collage-card">
              <img src={collage.imageUrl} alt={`Collage ${index}`} />
              <div className="collage-info">
                <p>Data: {formatDate(collage.date)}</p>
                <p>Tempo: {translatePeriod(collage.period)}</p>
                <button onClick={() => toggleExpand(index)}>
                  {expandedIndex === index ? 'HIDE TRACKS' : 'SHOW TRACKS'}
                </button>
                {expandedIndex === index && (
                  <ul className="track-list">
                    {collage.tracks.map((track, trackIndex) => (
                      <li key={trackIndex} style={{ marginBottom: '8px' }}>
                        <span>{track.artist.name} - {track.name}</span>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CollageCatalog;