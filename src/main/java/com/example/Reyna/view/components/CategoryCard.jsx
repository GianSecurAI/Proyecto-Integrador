import React from 'react';
import PropTypes from 'prop-types';
import '../styles/categorycard.css';

function CategoryCard({ image, title }) {
  return (
    <div className="card border-0 shadow-sm position-relative overflow-hidden category-card">
      <img src={image} className="card-img" alt={title} loading="lazy" />
      <div className="card-img-overlay d-flex justify-content-center align-items-center p-0">
        <span className="w-100 text-center category-card-title">
          {title}
        </span>
      </div>
    </div>
  );
}

CategoryCard.propTypes = {
  image: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
};

export default CategoryCard;