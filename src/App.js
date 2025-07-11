import './App.css';

import { ReactComponent as PlantLogo } from './icons/plant.svg';
import { ReactComponent as CartAdd } from './icons/cartAdd.svg'
import { ReactComponent as CartRm } from './icons/cartRm.svg'

import monstera from './monstera_deliciosa.png';
import { useState } from 'react';

const listOfProducts = [
  {name: "Monstera deliciosa", price: 100, img: monstera},
  {name: "Ficus benjamina", price: 100, img: monstera},
  {name: "Lavandula angustifolia", price: 100, img: monstera},
  {name: "Aristolochia grandiflora", price: 100, img: monstera},
  {name: "Serrulatocaulis polymorphus", price: 100, img: monstera}
];

function Product({name, price, img, addToCart}) {
  return (
    <div className='product'>
      <div className='product-background'/> 
      <div className='cart-add-animation'>
        <CartAdd className='cart-add' onClick={() => addToCart(name, price, img)}/>
        <img src={img} className="product-img" alt="Product"/>
      </div>
      <div className="product-name"> {name} </div>
      <div className="product-price"> {price} PLN</div>
    </div>
  );
}



function CartProduct({name, price, img, amount}) {
  return (
    <div className='cart-product'>
      <img src={img} className="cart-product-img" alt={name}/>
      <div className="cart-product-price"> {price} </div>
      <div className='cart-amount-manager'>
        <CartRm className='cart-rm'/>
        <div className="cart-product-amount"> {amount} </div>
      </div>
      
    </div>
  )
}

function Menu() {
  return (
    <div className='menu'>
      <PlantLogo className="shop-logo"/>

      <div className='search-bar'>
        <input className='search-bar-input' type="text" placeholder="Wyszukaj" required />
        <PlantLogo className='search-icon'></PlantLogo>
      </div>

      <div className="category-button">
        <button className="button">Kategorie ▼</button>
        <div className="dropdown-content">
          <a href="#" id="top">Algi</a>
          <a href="#">Mchy</a>
          <a href="#">Kwitnące</a>
          <a href="#" id="bottom">Grzyby</a>
        </div>
      </div>

      <button className='login-button'> Zaloguj </button>
      <div className='menu-border'></div>
    </div>
  )
}

function BottomBar() {
  return (
    <div className='bottom'>
      <div className='bottom-border'></div>
    </div>
  )
}

function Cart({productsInCart}) {
  return (
    <div className='checkout'>
        <div className="cart">
        <div className='cart-total'> Suma: </div>
        <div className='cart-products'>
          {productsInCart.map(product => (
            <CartProduct
              name={product.name}
              price={product.price}
              img={product.img}
              amount={product.amount}
            />
          ))}
        </div>
      </div>
      <button className='buy-button'> Kup </button>
    </div>
    
  )
}

function Products({productsInCart, addToCart}) {
  return (
    <div>
        <h1 className='products-h'>Wszystkie Rośliny </h1>
        <div className='products'>
          {listOfProducts.map(product => (
            <Product
              name={product.name}
              price={product.price}
              img={product.img}
              productsInCart={listOfProducts}
              addToCart={addToCart}
            />
          ))}
        </div>
        
    </div>
  )
}

export default function Shop() {
  const [productsInCart, add, remove] = useState([]);

  function removeFromCart(name) {
    
  }

  function addToCart(name, price, img) {
      add(item =>[...item, {name, price, img, amount: 1}]);
  }

  return (
    <div className='body'>
      <Menu />
      <Cart productsInCart={productsInCart}/>
      <Products productsInCart={productsInCart} addToCart={addToCart}/> 
      <BottomBar />
    </div>
  )
}