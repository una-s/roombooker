import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
// Увозимо компоненте за рутирање из 'react-router-dom' библиотеке
import { BrowserRouter, Routes, Route } from "react-router-dom";
// Увозимо све појединачне компоненте/странице из 'elementi' фолдера
import { Registracija } from './elementi/Registracija';
import { Login } from './elementi/Login';
import reportWebVitals from './reportWebVitals';
import { PocetnaRecepcionar } from './elementi/PocetnaRecepcionar';
import { DodajSobu } from './elementi/DodajSobu';
import { GostiRecepcionar } from './elementi/GostiRecepcionar';
import { PocetnaGosti } from './elementi/PocetnaGosti';
import { PravljenjeRezervacije } from './elementi/ZahteviRezervacije';
import { RezervacijeRecepcionar } from "./elementi/RezervacijeRecepcionar";
import { SobaDetalji } from "./elementi/SobaDetalji";
import { RezervacijeGost } from "./elementi/RezervacijeGost";


// Креирамо root елемент Реацт апликације
const root = ReactDOM.createRoot(document.getElementById('root'));
// Рендерујемо апликацију у strict режиму за боље детектовање потенцијалних проблема
root.render(
  <React.StrictMode>
    {/* BrowserRouter омогућава коришћење историје претраживача за рутирање */}
    <BrowserRouter>
      {/* Routes компонента садржи све дефинисане руте */}
      <Routes>
        {/* Рута за почетну страницу (за госте) */}
        <Route path="/" element={<PocetnaGosti />} />
        {/* Рута за страницу регистрације */}
        <Route path="/registracija" element={<Registracija />} />
        {/* Рута за страницу пријаве */}
        <Route path="/login/" element={<Login />} />
        {/* Рута за почетну страницу рецепционара */}
        <Route path="/recepcionar" element={<PocetnaRecepcionar />} />
        {/* Рута за страницу додавања нове собе */}
        <Route path="/dodaj_sobu" element={<DodajSobu />} />
        {/* Рута за страницу прегледа гостију (за рецепционара) */}
        <Route path="/gosti_recepcionar" element={<GostiRecepcionar />} />
        {/* Рута за страницу прављења резервације */}
        <Route path="/pravljenje_rezervacije" element={<PravljenjeRezervacije />} />
        {/* Рута за страницу прегледа резервација (за рецепционара) */}
        <Route path="/rezervacije" element={<RezervacijeRecepcionar />} />
        {/* Рута за детаљан приказ собе, са динамичким параметром idSoba */}
        <Route path="/soba/:idSoba" element={<SobaDetalji />} />
        {/* Рута за страницу резервација појединог госта */}
       <Route path="/rezervacije-gost" element={<RezervacijeGost />} />

      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

// reportWebVitals функција за мерење перформанси апликације (опционо)
reportWebVitals();