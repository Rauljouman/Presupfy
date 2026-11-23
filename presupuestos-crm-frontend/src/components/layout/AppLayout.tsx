
import { useState } from 'react'
import { Outlet } from 'react-router'
import Sidebar from './Sidebar'
import Header from './Header'

function AppLayout() {
  const [isMenuOpen, setIsMenuOpen] = useState(false)

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar
        isOpen={isMenuOpen}
        onClose={() => setIsMenuOpen(false)}
      />

      <div className="flex min-w-0 flex-1 flex-col">
        <Header
          isMenuOpen={isMenuOpen}
          onMenuClick={() => setIsMenuOpen(true)}
        />

        <main className="min-w-0 flex-1 px-4 py-6
          md:px-6 lg:px-8 lg:py-8">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default AppLayout
