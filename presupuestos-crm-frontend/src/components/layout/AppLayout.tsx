
import { useState } from 'react'
import { Outlet } from 'react-router'

import Sidebar from './Sidebar'
import Header from './Header'
import Footer from './Footer'

function AppLayout() {
  const [isMenuOpen, setIsMenuOpen] = useState(false)

  return (
    <div className="flex min-h-screen bg-background">

      {/* SIDEBAR */}
      <Sidebar
        isOpen={isMenuOpen}
        onClose={() => setIsMenuOpen(false)}
      />

      {/* CONTENIDO DEL CRM */}
      <div className="flex min-w-0 flex-1 flex-col">

        {/* HEADER */}
        <Header
          isMenuOpen={isMenuOpen}
          onMenuClick={() => setIsMenuOpen(true)}
        />

        {/* CONTENIDO DE CADA RUTA */}
        <main className="min-w-0 flex-1 px-4 py-6 md:px-6 lg:px-8 lg:py-8">
          <Outlet />
        </main>

        {/* FOOTER */}
        <Footer variant="light" />

      </div>
    </div>
  )
}

export default AppLayout
