import './App.css';

import { ReactComponent as PlantLogo } from './icons/plant.svg';
import { ReactComponent as CartAdd } from './icons/cartAdd.svg'
import { ReactComponent as CartRm } from './icons/cartRm.svg'

import { useEffect, useState, userEffect } from 'react';

const listOfProducts = [
  {name: "Monstera deliciosa", price: 100, img: "https://drive.google.com/thumbnail?id=1m_OtY8vDV3xvBC4bOGnBdZ717ZOWD8Nx"},
  {name: "Monstera deliciosa", price: 100, img: "https://drive.google.com/thumbnail?id=1m_OtY8vDV3xvBC4bOGnBdZ717ZOWD8Nx"},
  {name: "Lavandula angustifolia", price: 100, img: "https://drive.google.com/thumbnail?id=1f-2xe-4sm2OQTRmk4dg8X17nERuaekLh"},
  {name: "Monstera deliciosa", price: 100, img: "https://drive.google.com/thumbnail?id=1m_OtY8vDV3xvBC4bOGnBdZ717ZOWD8Nx"},
  {name: "Monstera deliciosa", price: 100, img: "https://drive.google.com/thumbnail?id=1m_OtY8vDV3xvBC4bOGnBdZ717ZOWD8Nx"},
  {name: "Euphorbia tithymaloides", price: 66.66, img: "https://drive.google.com/thumbnail?id=1FmIImcblmr71b8RdvfZXa5Sr7OuOr5TU"}
];

function Product({name, price, img, addToCart}) {
  console.log(img);

  return (
    <div className='product'>
      <div className='product-background'/> 
      <div className='cart-add-animation'>
        <CartAdd className='cart-add' onClick={() => addToCart(name, price, img)}/>
        <img src={img} className="product-img" alt="Plant"/>
      </div>
      <div className="product-name"> {name} </div>
      <div className="product-price"> {price} PLN</div>
    </div>
  );
}



function CartProduct({name, price, img, amount, removeFromCart}) {
  return (
    <div className='cart-product'>
      <img src={img} className="cart-product-img" alt={name}/>
      <div className="cart-product-price"> {price} </div>
      <div className='cart-amount-manager'>
        <CartRm className='cart-rm' onClick={() => removeFromCart(name)}/>
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

function Cart({productsInCart, removeFromCart}) {
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
              removeFromCart={removeFromCart}
            />
          ))}
        </div>
      </div>
      <button className='buy-button'> Kup </button>
    </div>
    
  )
}

function ProductList({addToCart}) {
  return (
    <div>
        <h1 className='products-h'>Wszystkie Rośliny </h1>
        <div className='products'>
          {listOfProducts.map(product => 
            <Product
              name = {product.name}
              price = {product.price}
              img = {product.img}
              addToCart = {addToCart}
            />
          )}
        </div>
        
    </div>
  )
}


export default function Shop() {
  const [productsInCart, update] = useState([]);

  // useEffect(() => {
  //   fetch('')
  // })

  function addToCart(name, price, img) {
    const product = productsInCart.find(item => item.name == name)
    if (product) {
      update(item => item.map(item => item.name == name ? {...item, amount : product.amount + 1} : item))
    } else {
      update(item =>[...item, {name, price, img, amount: 1}])
    }
  }

  function removeFromCart(name) {
    const product = productsInCart.find(item => item.name == name)
    if (product.amount > 1) {
      update(item => item.map(item => item.name == name ? {...item, amount : product.amount - 1} : item))
    } else {
      update(list => list.filter(item => item.name !== name))
    }
  }

  return (
    <div className='body'>
      <Menu />
      <Cart productsInCart={productsInCart} removeFromCart={removeFromCart}/>
      <ProductList addToCart={addToCart}/> 
      <BottomBar />
    </div>
  )
}