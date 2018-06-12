background-image: url(images/welcome.png)
class: title-with-image, dark, cover

# Créer une monade…

## …Sans parler de monades

---

background-image: url(images/limonade.jpg)
class: title-with-image, light, contain

# Qu'est-ce qu'une monade ?

---

# Définition formelle

> * Un constructeur de type appelé type monadique, qui associe au type `t` le type `Mt`
> * Une fonction nommée `unit` ou `return`
    qui construit à partir d'un élément de type sous-jacent `a` un autre objet de type monadique `Ma`.
    Cette fonction est alors de signature return : `t → Mt`
> * Une fonction `bind`, représentée par l'opérateur infixe `>>=`,
    associant à un type monadique et une fonction d'association un autre type monadique.
    Il permet de composer une fonction monadique à partir d'autres fonctions monadiques.
    Cet opérateur est de type `>>=: Mt → ( t → Mu ) → Mu`
> 
> — https://fr.wikipedia.org/wiki/Monade_(informatique)

---
background-image: url(images/faint.gif)

---
class: title

# Et si nous parlions d'autre chose ? 😅

---
class: title

# Retour au besoin

## API avec gestion multitenant

---
# Besoins

* L'URL doit être paramétrable en configuration
* Chaque utilisateur doit pouvoir spécifier la langue dans laquelle exécuter les requêtes
  Exemple : Selon la langue de la requête entrante

--

* Et aussi, si vous avez un 500, relancer la requête une fois au cas où que ça marche

🤔 Et en investiguant :

* Et puis, pour certaines actions, ne pas relancer uniquement l’appel échouant, mais l’ensemble des requêtes

---
background-image: url(images/faint.gif)

---
class: title

# Comment structurer le code ?

## Avec une monade !

---
class: title-with-background
background-image: url(images/say-monad.jpg)

---
class: title

# Revenons à nos ~~monades~~ moutons

## Stratégie

---

# Ce que je connais

* Les traits et classes
* Le pattern decorator
* Comment fonctionne les for-comprehension scala

---

# Stratégie

* Je peux demander des paramètres externes en déclarant un contexte dans lequel je travaillerai
* Ce contexte sera alors explicité via le **type de sortie**
* Le contexte envahira le reste du code
* Seule la fonction de plus haut niveau (ex: Action http) donnera les informations nécessaires

---

# For-comprehension en scala

```scala
for {
  createdOrder <- api.post[Order, OrderId]("/orders", input)
  createdOrderId = createdOrder.id
  receipt <- api.post("/orders/receipt", createdOrderId)
} yield receipt
```

Équivalent à :

```scala
api.post[Order, OrderId]("/orders", input)
  .map { createdOrder => createdOrder.id }
  .flatMap { createdOrderId => api.post("/orders/receipt", createdOrderId) }
```

---
class: title

## Live code reading 👓

---
class: title-with-image, dark
background-image: url(images/amazing.gif)

# Vous venez de créer une monade !

---

# Vous venez de créer une monade !

La preuve :

> * Un constructeur de type appelé type monadique, qui associe au type `t` le type `Mt`

```scala
trait Context[A]
```

---

# Vous venez de créer une monade !

La preuve :

> * Une fonction nommée `unit` ou `return` qui construit à partir d'un élément de type sous-jacent `a` un autre objet de type monadique `Ma`.
    Cette fonction est alors de signature return : `t → Mt`.

```scala
// Scala convention: pure = unit/return
def pure[A](value: A): Context[A]
```

---

# Vous venez de créer une monade !

La preuve :

> * Une fonction `bind`, représentée par l'opérateur infixe `>>=`, associant à un type monadique et une fonction d'association un autre type monadique.
    Il permet de composer une fonction monadique à partir d'autres fonctions monadiques.
    Cet opérateur est de type `>>=: Mt → ( t → Mu ) → Mu`.

```scala
// Scala convention: flatMap = bind
// Remember: already in Mt
trait Context[A] {
  // ( t → Mu ) → Mu
  def flatMap[B](op: A => Context[B]): Context[B]
}
```

---

# Aller plus loin

Puisque que c'est un simple pattern decorator, il est possible d'ajouter d'autres fonctions :

```scala
trait Context[A] {
  def retryOnce: Context[A] = ContextRetryOnce(this)
  
  def retryAllOnce: Context[A] = ContextRetryAllOnce(this)
  
  def recover(pf: PartialFunction[Throwable, U]): Context[A] =
    ContextRecover(this, pf)
}

object Context {
  def fromFuture[A](future: Future[A]): Context[A] = ContextFuture(future)
  
  def requiring[A](option: Option[A], errorOnMissing: String): Context[A] =
    ContextRequiring(option, errorOnMissing)
}
```

---

# Aller plus loin

Et donc de répondre aux derniers besoins :

> * Et aussi, si vous avez un 500, relancer la requête une fois au cas où que ça marche
> * Et puis, pour certaines actions, ne pas relancer uniquement l’appel échouant, mais l’ensemble des requêtes

---

# TL;DL

```scala
// Interface simple

trait Monad[A] {
  def flatMap[B](op: A => Monad[B]): Monad[B]
}
object Monad {
  def pure[A](value: A): Monad[A]
}
```

* Peut être réalisée par le pattern decorator
* Est **une base** pour créer des objets plus complexes
* Facilement composable et exploitable dans les for-comprehensions