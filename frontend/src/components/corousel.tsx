import React, { useState, useEffect } from 'react';
import './carousel.css'; // Certifique-se de importar o arquivo CSS

interface CarouselProps {
  messages?: string[];
}

const Carousel: React.FC<CarouselProps> = ({ messages = ["Estamos preparando sua colagem...📀", "Selecionando suas músicas...🎶", "Estamos quase lá!! 🐼"] }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [fade, setFade] = useState<'fade-in' | 'fade-out'>('fade-in');

  useEffect(() => {
    const interval = setInterval(() => {
      setFade('fade-out');
      setTimeout(() => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % messages.length);
        setFade('fade-in');
      }, 500); 
    }, 2000);

    return () => clearInterval(interval);
  }, [messages.length]);

  return (
    <div className="carousel">
      <p className={fade}>{messages[currentIndex]}</p>
    </div>
  );
};

export default Carousel;