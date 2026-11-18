import $ from 'jquery'
export function getElement(seletor) {
  return new Promise((resolve, reject) => {
    let time = 0
    const interval = setInterval(() => {
      time++
      if (time > 10) {
        clearInterval(interval)
        reject(new Error('fail to init '))
      } else {
        if ($(seletor)) {
          clearInterval(interval)
          resolve($(seletor))
        }
      }
    }, 100)
  })
}
